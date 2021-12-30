package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    //화면에 맞는 FormData를 만들고, 데이터를 처리하는 것을 권장(Entity와 완벽하게 일치하지 않음 -> Entity에서 처리시 @Valid 등 지저분해지기 쉬움)

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;

    private String city;

    private String street;

    private String zipcode;

}
