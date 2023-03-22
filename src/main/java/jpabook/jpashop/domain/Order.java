package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // ORDER 가 예약어(키워드)여서 오류가 발생하는
@Getter
@Setter // DB가 있어서 안전하게 테이블 이름을 바꿔놓은 것 같아요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) // Member와 1:N 관계 Order이 M이니까 ManyToOne
  @JoinColumn(name = "member_id") // 맵핑을 무엇으로 할거냐, FK이름이 member_id가 된다.
  private Member member;
  // orders가 주인이기 때문에 그냥두고 member에 mappedBy 사용
  /*
  Member는 Orders를 리스트로 가지고 있다. Orders도 Member를 가지고 있다.
  양방향 참조가 일어난 것
  데이터베이스  FK는 Orders의 member_id 하나 뿐이 없다
  문제는 Member, Order의 관계를 바꾸고싶으면, FK의 값을 변경해야한다
  but 엔티티에는 양쪽다 필드를 가지고 있다 -> JPA는 혼동이 온다
  여기서 FK업데이트 하는것을 둘중 하나만 선택하도록 규약
  객체는 변경포인트가 두곳이지만 테이블은 한쪽테이블의 FK하나만 변경하면됨
  이때 한쪽 테이블을 정하기 위해
  두 테이블중 하나를 주인 개념으로 잡는다 -> 연관관계 주인
   */

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  /*
   cascade = CascadeType.ALL
   모든 엔티티는 persist를 각자해야한다.
   때문에 entity당 각각 persist를 해야한다.
   persist(orderItemA)
   persist(orderItemB)
   persist(orderItemC)
   persist(order)

   하지만 cscade를 두면
   persist(order)만 해주면 알아서 persist전파해준다.
   또한 ALL로 해놨기때문에 delete시에도 같이 지워준다.
  */
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate; // 주문시간

  @Enumerated(EnumType.STRING)
  private OrderStatus status; // 주문상태 [ORDER, CANCEL]

  // ==연관관계 편의 메서드==//
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  /*
  원래는 비지니스 로직에서 처리해줘야함..
  이걸 해결하기 위해 연관관계 편의 메서드 사용
  public static void main(String[] args) {
      Member member = new Member();
      Order order = new Order();

      member.getOrders().add(order);
      order.setMember(member);
  }
  */

  /** 핵심 비즈니스 로직 */
  // ==생성 메서드==//
  // 생성할 때 무조건 createOrder를 호출해야한다.
  // 주문 생성에대한 복잡한 비즈니스 로직을 완결
  // -> 생성하는 관련 바꾸는건 이것만 바꾸면 된다.
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order();
    order.setMember(member);
    order.setDelivery(delivery);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }

  // ==비즈니스 로직==//
  /** 주문 취소 */
  public void cancel() {
    if (delivery.getStauts() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
    }
    // 배송시작 안했으면 주문 취소 (상태 변경)
    this.setStatus(OrderStatus.CANCEL);
    // 재고 관리
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }

  // ==조회 로직==//
  /** 전체 주문 가격 조회 */
  public int getTotalPrice() {
    int totalPrice = 0;
    for (OrderItem orderItem : orderItems) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }
  //    public int getTotalPrice() {
  //        return orderItems.stream()
  //            .mapToInt(OrderItem::getTotalPrice)
  //            .sum();
  //    }
}
