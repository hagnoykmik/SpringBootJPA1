package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 상품 등록
     */
    public void save(Item item) {
        if (item.getId() == null) {   // id값이 없으면 새로 생성한 객체(존재하지 않는 상품)
            em.persist(item);
        } else {                      // 이미 db에 등록되어있는 상품 -> update
            em.merge(item);
        }
    }

    /**
     * 상품 단건 조회
     */
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 상품 모두 조회
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
