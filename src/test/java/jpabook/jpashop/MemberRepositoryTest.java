package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)                    //JUnit에 스프링과 관련된 테스트를 하겠다고 알려줌
@SpringBootTest                                 //스프링부트 테스트를 하겠다
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional                              //javax가 아닌 스프링으로 쓰는 것을 권장, 옵션이 많음
    @Rollback(false)                          //롤백 방지
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);        //같은 트랜젝션, 같은 영속성 컨텍스트이므로 동일
    }
    //Spring에 의해 Test가 끝난 후 DB는 자동으로 RollBack됨

}