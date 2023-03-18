package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
// 싱글테이블이기 때문에 DB입장에서 저장될때 구분이 되어야한다.
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;

}
