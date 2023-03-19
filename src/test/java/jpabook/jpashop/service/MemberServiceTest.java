package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


//RunWith(SpringRunner.class) ==junit실행할 때 , 스프링이랑 같이 실행할래 (@SpringBootTest에 포함)
@SpringBootTest // 스프링 부트를 띄운상태로 테스트를 실행할 때 (스프링컨테이너 안에서 테스트 돌리기)
@Transactional  // 같은 트랜잭션 안에서 같은 엔티티(pk값이 똑같으면)면 같은 영속성 컨텍스트에서 같은 애로 관리된다. -> Spring에서 디폴트가 rollback(테스트 끝나면)
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false) // rollback안하고 commit함 -> em.flush()로 영속성 컨텍스트에 insert
    public void 회원가입() throws Exception {
        //given(이런게 주어졌을 때)
        Member member = new Member();
        member.setName("kim");

        //when(이렇게 하면)
        Long savedId = memberService.join(member);

        //then(이렇게 된다)
        assertEquals(member, memberRepository.findOne(savedId));  // 같은 회원인지
    }

    @Test
    public void 중복회원예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("beom");

        Member member2 = new Member();
        member2.setName("beom");

        //when
        memberService.join(member1);
        // junit4
        try {
            memberService.join(member2);         // 예외가 발생해야 한다.
        } catch (IllegalStateException e) {      // exception이 되면 정상적으로 return
            return;
        }

        // junit5
//        //then
//        assertThatTrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class).


        /**
         * IllegalArgumentException
         * 잘못된 인수를 가진 호출을 받았을 때 사용
         * 사용자의 입력이 형식에 어긋난 경우의 예외처리
         *
         * IllegalStateException
         * 호출받은 객체가 요청을 수행하기에 적합하지 않은 상태일 때 사용
         * 중복된 회원
         */

        //then
        fail("예외가 발생해야 합니다.");  // 코드가 여기 오면 안된다(실패)
    }

}