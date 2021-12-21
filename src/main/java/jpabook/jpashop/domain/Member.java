package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    /**
     * [ Spring Boot의 테이블, 컬럼명 생성 전략 ]
     *
     * - hibernate 기존 구현 : 엔티티 필드명을 그대로 테이블명으로 사용
     * (SpringPhysicalNamingStrategy)
     *
     * - Spring Boot의 신규 설정 => Entity(Field) -> Table(Column)
     * (1) CamelCase => UnderScore( 예 : memberPoint -> member_point)
     * (2) .(dot) => _(UnderScore)
     * (3) UpperCase => LowerCase
     * 
     * spring.jpa.hibernate.naming.implicit-strategy : 논리명 생성 ; ** 테이블이나 컬럼명 명시하지 않을 때 ** 논리명적용
     * spring.jpa.hibernate.naming.physical-strategy : 물리명 적용 ; ** 모든 논리명(명시 내용 포함) **에 적용, 실제 테이블에 적용
     * 을 통해서 회사 룰로 변경 가능
     */

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
