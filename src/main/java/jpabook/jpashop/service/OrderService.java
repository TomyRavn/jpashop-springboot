package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());   //실제는 배송지 정보 따로 입력 받아야 함

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);  //유지보수를 위해 createOrderItem 방식 외의 setter 등의 생성방식은 막을 필요가 있음

        /* 생성 불가 => PROTECTED를 통해 제약(유지보수에 용이)
         * OrderItem tempItem = new OrderItem();
         */

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);        //Order의 orderItems와 delivery에 cascade 옵션이 걸려있기 때문에 같이 자동으로 persist됨
                                            //다른 데서 쓰지 않을 때만 cascade 옵션을 거는 것을 권장(Order만 OrderItem, Delivery를 사용)
        
        //Order 식별자값 반환
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        /*
         * JPA의 강점 : SQL을 따로 짤 필요없이, JPA가 DIRTY CHECKING( 또는 변경력 감지 )을 통해 UPDATE QUERY 전달
         */

        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }
    
    //검색
    //public List<Order> findOrders(OrderSearch orderSearch){
    //    return orderRepository.findAll(orderSearch);
    //}


    /**
     * - 서비스 내 로직이 존재하는 패턴  : 트랜젝션 스크립트 패턴 (예 : 기존 Spring 프로젝트)
     * - Entity 내 로직이 존재하는 패턴 : 도메인 모델 패턴      (예 : JPA, ORM 등 객체 지향 프로젝트)
     */

}
