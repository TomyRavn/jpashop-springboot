package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
//    @Autowired
//    EntityManager em;

    //@Rollback(false)
    @Test
    public void 회원가입() throws Exception {
        //given (이것이 주어졌을 때)
        Member member = new Member();
        member.setName("kim");

        //when (이렇게 하면)
        Long savedId = memberService.join(member);

        //then (이렇게 된다)
        //em.flush();                                                 //Insert 쿼리 확인
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)           //expected 속성 설정해주면 아래 주석된 코드를 생략할 수 있다.
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        //try{
            memberService.join(member2);        //동일한 이름 가입으로 예외 발생해야 함!
        //} catch (IllegalStateException e){
        //    return;
        //}

        //then
        fail("예외가 발생해야 한다.");
    }

}