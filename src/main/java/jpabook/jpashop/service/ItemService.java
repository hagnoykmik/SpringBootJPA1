package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    public final ItemRepository itemRepository;

    /**
     * 상품 등록
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 변경 감지 기능 (준영속 엔티티 수정)
     */
    @Transactional // 세팅다하면 @Transactional에 의해서 commit이 된다. -> JPA는 flush -> 영속성 컨텍스트에 있는 엔티티중에 변경된 애가 누군지 다 찾는다 -> 바뀐값을 update쿼리를 DB에 날린다.
    public void updateItem(Long itemId, UpdateItemDto itemDto) {
        Item findItem = itemRepository.findOne(itemId); // id를 기반으로 실제 DB에 있는 영속 상태 엔티티(findItem)를 찾아옴
//        findItem.change(price, name, stockQuantity);
        findItem.setPrice(itemDto.getPrice());
        findItem.setName(itemDto.getName());
        findItem.setStockQuantity(itemDto.getStockQuantity());
//        itemRepository.save(findItem); 을 할 필요가 없다
    }

    /**
     * 상품 목록 조회
     */
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     *  상품 조회
     */
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
