package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //JPA 내장타입 어딘가에 내장 될수 있다
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //JPA가 기본 생성할때 기본생성자가없으면 프록시나 리플렉션을 쓰지못해서 에러난다
    protected Address() {
    }

    // 값타입은 변경이 불가능해야한다. 좋은 설계가 생성할 때만 세팅되고 세터 제공X
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
