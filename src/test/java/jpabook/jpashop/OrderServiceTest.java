package jpabook.jpashop;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {

  @Autowired EntityManager em;
  @Autowired OrderService orderService;
  @Autowired OrderRepository orderRepository;

  @Test
  public void 상품주문() throws Exception {

    //given
    Member member = createMember();

    Book book = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;

    //when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    //then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals(OrderStatus.ORDER, getOrder.getStatus(),"상품 주문시 상태는 ORDER");  // 기대값, 실제값, 메세지
    assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
    assertEquals(10000 * orderCount, getOrder.getTotalPrice(),"주문 가격은 가격 * 수량이다.");
    assertEquals(8, book.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다.");
  }

  @Test
  public void 주문취소() throws Exception {

    //given(준비된것)
    Member member = createMember();
    Book item = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

    //when(실제 내가 test 하고 싶은것)
    orderService.cancelOrder(orderId);

    //then
    Order getOrder = orderRepository.findOne(orderId);

    assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL이다.");
    assertEquals(10,item.getStockQuantity() ,"주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
  }

  @Test
  public void 상품주문_재고수량초과() throws Exception {

    //given
    Member member = createMember();
    Item item = createBook("시골 JPA", 10000, 10);

    int orderCount = 11;

    //when

    //then
    assertThrows(NotEnoughStockException.class, () -> {
      orderService.order(member.getId(), item.getId(), orderCount);
    }, "재고 수량 부족 예외가 발생해야 한다.");

    /*
    Assertions.assertThrows() 에서 사용자가 예측한 오류가 던져질 경우엔 테스트 성공으로 처리되며 프로세스가 중단되지 않고 다음 프로세스를 진행하게 됩니다.
    테스트가 실패하면 그 테스트는 종료되지만 테스트가 통과한 경우에는 테스트의 끝 (@Test가 붙은 함수의 끝) 까지 프로세스를 계속 진행하게 됩니다.
    JUnit5의 경우 NotEnoughStockException이 예측한 오류여서 테스트가 통과된 후에 fail("재고수량 부족~")으로 넘어가기 때문에  테스트가 fail된다
    */
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울", "강가", "123-123"));
    em.persist(member);
    return member;
  }
}