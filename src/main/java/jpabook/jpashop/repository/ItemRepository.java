package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){       //새로 생성한 객체는 persist로 신규 등록
            em.persist(item);
        }else{                          //DB에서 가져온 것을 update(merge는 완전한 update의 개념은 아님)
            em.merge(item);
            // merge : find한 값을 parameter의 값으로 전부 update
            // ==> 주의점 : 모든 속성이 변경됨 / 값이 없으면 null로 update되는 위험 (*****) / 변경 감지가 아님
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
