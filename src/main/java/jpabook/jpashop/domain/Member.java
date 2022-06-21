package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    //@JsonIgnore //API 제공 시 Entity에서 제거할 항목을 지정 가능, 하지만 Entity를 외부에 노출하는 것은 좋지 않음
    //API마다 필요한 사항이 달라서 문제 발생 가능 + 프레젠테이션 계층이 Entity에 추가되어 애플리케이션 수정 시 문제 발생(모든 Case에 적용 불가)
    //Entity 변경 시 API 스펙이 변하므로 장애 발생
    //유연성이 떨어짐(기본 Array와 별개의 속성 추가 시 JSON 스펙이 깨지는 문제)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
