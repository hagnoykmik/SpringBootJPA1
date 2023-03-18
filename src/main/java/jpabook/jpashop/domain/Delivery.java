package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    // EnumType은 default가 ORDINNAL
    // 컬럼이 1 2 3 4 숫자로 들어감 READY는 1, COMP는 2 이런식으로
    // 중간에 다른상태가 생기면 ex) READY, XXX, COMP -> 1, 2, 3 밀리게됨
    // DB조회하면 값이다변함. ORDINAL 사용 X -> STRING사용
    private DeliveryStatus stauts; //READY, COMP
}
