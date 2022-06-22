package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore //양방향 연관관계에서 API 생성 시, Entity를 바로 접근한다면 한 쪽은 Ignore 처리 해야 함
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;     //주문 가격

    private int count;          //주문 수량


    /**
     * OrderItem orderItem = new OrderItem();
     * orderItem.setItem();
     * 과 같이 퍼지는 것을 막기 위함(유지보수가 어려워짐)
     * ( ***** )
     * JPA에서 protected는 위의 방법을 쓰지 말라는 뜻
     *
     * //protected OrderItem() {}
     * ==> Lombok의 @NoArgsConstructor 어노테이션 기능으로 동일 처리 가능
     */

    // === 생성 메서드 === //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        //할인 등 바뀔 수 있기 때문에 Item과는 따로 가져가는 게 맞음
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // === 비즈니스 로직 === //
    public void cancel() {
        //재고수량 원상복구
        getItem().addStock(count);
    }

    // === 조회 로직 === //
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
