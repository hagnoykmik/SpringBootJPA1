package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue // @Id 현재 엔티티의 PK값으로 선언 @Gener.. 기본키 할당 방법 선택
    @Column(name = "member_id") // 없으면 이름이 그냥 id가 되어버림
    private Long id;

    private String name;

    @Embedded // 내장타입을 포함했다는 어노테이션으로 맵핑
    private Address address; // Alt + Enter -> create class

    @OneToMany(mappedBy = "member") // Order와 1:N 관계 Member가 1이니까 OneToMany
    // (mappedBy = "member") orders테이블에 있는 member필드에 의해 맵핑 된거야라고 표시
    private List<Order> orders = new ArrayList<>(); // Alt + Enter -> create class
}
