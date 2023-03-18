package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") // hello라는 url로오면 이컨트롤러 호출하겠다.
    public String hello(Model model){ // model에 어떤 데이터를 담아 컨트롤러가 view로 넘길수 있다.
        model.addAttribute( "data", "hello!!");
        return "hello"; //resource.templates.hello.html으로
    }
}
