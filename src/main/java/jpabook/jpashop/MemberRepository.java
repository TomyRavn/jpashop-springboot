package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
        //ID로만 반환하는 이유 : 개인적인 Style(Command 와 Query를 분리)
        //저장을 하고나면 Side Effect 방지를 위해 Return을 하지 않지만, ID값만 있으면 재조회가 가능하므로 ID만 반환
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
