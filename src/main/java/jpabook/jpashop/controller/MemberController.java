package jpabook.jpashop.controller;

import javax.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  // 회원 가입 페이지
  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());
    /*
     controller에서 view로 넘어갈때 이 데이터를 실어서 넘긴다.
     */
    return "members/createMemberForm";
  }

  // 회원 등록 (summit)
  @PostMapping("/members/new")
  public String create(@Valid MemberForm form, BindingResult result) {
    /*
    @Valid - 객체의 제약 조건을 검증
    @Valid 뒤에 BindingResult가 있으면 오류가 result에 담겨서 아래 코드가 실행됨.
     */
    if (result.hasErrors()) {  // 에러가 있으면
      return "members/createMemberForm";
    }

    Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

    Member member = new Member();
    member.setName(form.getName());
    member.setAddress(address);

    memberService.join(member);  // 저장
    return "redirect:/";         // 저장이 끝나면 첫번째 페이지로 이동
  }

  // 회원 목록 조회
  @GetMapping("/members")
  public String list(Model model) {  // Model : Controller에서 생성한 데이터를 담아서 View로 전달할 때 사용하는 객체, map 구조로 저장됨(key와 value로 구성)
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);  // model.addAttribute("키", 값) -> 전달할 데이터 세팅
    return "members/memberList";
  }
}
