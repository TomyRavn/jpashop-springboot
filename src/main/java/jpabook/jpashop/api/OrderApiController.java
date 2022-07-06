package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    //Entity 직접 노출 + 양방향 연관관계에 JSONIgnore 모두 처리 필요
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            
            //OrderItems 프록시 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            //OrderItem 내부의 item도 초기화
            /*
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
            */
            //=> Lambda
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return all;
    }

}
