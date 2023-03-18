package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 상속관계 맵핑이기 때문에 상속관계 전략을 지정해야한다
// 부모클래스에 잡아줘야한다. 우리는 싱글테이블 전략을 쓴다
@DiscriminatorColumn(name = "dtype")
// 구분해준다 book이면 이걸 할꺼야! -> book에 @DiscrimnatorValue
@Getter @Setter
public abstract class Item { //abstract 추상클래스로 생성 구현체를 가지고 할것이기 때문

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
