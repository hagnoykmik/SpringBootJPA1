package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록
    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
//      Book book = new Book();
//      book.setName(form.getName());
//      book.setPrice(form.getPrice());
//      book.setStockQuantity(form.getStockQuantity());
//      book.setAuthor(form.getAuthor());
//      book.setIsbn(form.getIsbn());

        // 이렇게 하는 것 추천
        Book book = Book.createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.saveItem(book);

        return "redirect:/items";
    }

    // 상품 목록 조회
    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    // 상품 수정 form
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();  // update할 때 form을 보낼거다.
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    // @ModelAttribute - 사용자가 요청시 전달하는 값을 오브젝트 형태로 매핑해주는 어노테이션
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

        // 어설프게 엔티티를 파라미터로 안씀
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());

//        itemService.saveItem(book);
        UpdateItemDto itemDto = new UpdateItemDto();
        itemService.updateItem(itemId, itemDto);
        return "redirect:/items";
    }
}
