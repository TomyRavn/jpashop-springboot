package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor            //3. 2번이 가능하기 때문에 Lombok 적용시 가능, Service와 동일한 형태로 일관성
public class MemberRepository {

    //1. 정식
    //@PersistenceContext
    //private EntityManager em;

    //2. Spring Boot 사용 시 가능 (*****)
    //@Autowired
    //private EntityManager em;

    private final EntityManager em;

    /**
     * Factory
     */
    //@PersistenceUnit
    //private EntityManagerFactory emf;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
