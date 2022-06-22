package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    //XToOne 관계는 Default가 즉시로딩(EAGER)이므로 직접 지연로딩(LAZY)로 설정해야 함
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    @JsonIgnore //양방향 연관관계에서 API 생성 시, Entity를 바로 접근한다면 한 쪽은 Ignore 처리 해야 함
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;  //READY, COMP

}
