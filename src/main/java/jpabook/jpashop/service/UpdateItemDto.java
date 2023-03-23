package jpabook.jpashop.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateItemDto{

  private int price;
  private String name;
  private int stockQuantity;

  public static UpdateItemDto create(int price, String name, int stockQuantity) {
    return UpdateItemDto.builder()
        .price(price)
        .name(name)
        .stockQuantity(stockQuantity)
        .build();
  }
}
