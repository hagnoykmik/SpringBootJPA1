package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
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

    //==비즈니스 로직==//
    // 데이터를 가지고 있는 쪽에 메서드를 가지고 있는다
    // 엔티티 자체가 해결할 수 있는건 엔티티안에 넣는것이 좋음 (객체 지향적)
    // 값 변경은 핵심 비즈니스 메서드를 가지고 변경 (setter X)

    /**
     * 재고 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    /**
     * 변경 메서드
     */
//    public void change(int price, String name, int stockQuantity) {
//
//    }
}
