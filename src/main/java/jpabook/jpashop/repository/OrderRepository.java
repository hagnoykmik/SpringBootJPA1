package jpabook.jpashop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;

  public void save(Order order) {
    em.persist(order);
  }

  /**
   * 단건 조회
   */
  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }


  /**
   * 상품 검색
   */
//  public List<Order> (OrderSearch orderSearch) {
//
//  }
}
