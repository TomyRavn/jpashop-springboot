package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;

    private String street;

    private String zipcode;

    //JPA 스펙을 위해 기본 생성자 따로 설정
    //public 보다 안전하게 설정
    protected Address() {
    }

    //값 타입은 변경 불가능하게 설계
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
