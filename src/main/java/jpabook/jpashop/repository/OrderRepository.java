package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){
        /** 일반적인 쿼리(동적 파라미터 해결 불가)
         return em.createQuery("select o from Order o join o.member m" +
         " where o.status = :status" +
         " and m.name like :name", Order.class)
         .setParameter("status", orderSearch.getOrderStatus())
         .setParameter("name", orderSearch.getMemberName())
         .setFirstResult(1)              //OFFSET
         .setMaxResults(1000)            //LIMIT
         .getResultList();
         */

        /**
         * 동적 쿼리 해결 방법
         */
        /* 1. JPQL에 조건문 적용 */
//        String jpql = "select o from Order o join o.member m";
//        boolean isFirstCondition = true;
//
//        //주문 상태 검색
//        if(orderSearch.getOrderStatus() != null){
//            if(isFirstCondition){
//                jpql += " where";
//                isFirstCondition = false;
//            }else{
//                jpql += " and";
//            }
//            jpql += " o.status = :status";
//        }
//
//        //회원 이름 검색
//        if(StringUtils.hasText(orderSearch.getMemberName())){
//            if(isFirstCondition){
//                jpql += " where";
//                isFirstCondition = false;
//            }else{
//                jpql += " and";
//            }
//            jpql += " m.name like :name";
//        }
//
//        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
//                .setMaxResults(1000);
//
//        //파라미터 바인딩 동적 처리
//        if(orderSearch.getOrderStatus() != null) query = query.setParameter("status", orderSearch.getOrderStatus());
//        if(StringUtils.hasText(orderSearch.getMemberName())) query = query.setParameter("name", orderSearch.getMemberName());
//
//        return query.getResultList();

        /* 2. JPA Criteria(JPA가 동적 쿼리를 빌드하기 위해 표준으로 제공)
        *  ===> 치명적인 단점 : 어떤 쿼리가 만들어질 지 알기가 어려움 => 유지 보수성 ↓
        */
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();

        /* 3. Querydsl로 처리 - 1 (*****) */
//        QOrder order = QOrder.order;
//        QMember member = QMember.member;
//
//        return query
//                .select(order)
//                .from(order)
//                .join(order.member, member)
//                .where(statusEq(orderSearch.getOrderStatus()),
//                        nameLike(orderSearch.getMemberName()))
//                .limit(1000)
//                .fetch();
    }

        /* 3. Querydsl로 처리 - 2 */
//    private BooleanExpression statusEq(OrderStatus statusCond){
//        if(statusCond == null) return null;
//        return order.status.eq(statusCond);
//    }
//
//    private BooleanExpression nameLike(String nameCond){
//        if(!StringUtils.hasText(nameCond)) return null;
//        return member.name.like(nameCond);
//    }
}
