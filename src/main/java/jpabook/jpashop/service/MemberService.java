package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // read가 많을 때
@RequiredArgsConstructor
public class MemberService {

//    @Autowired  // 1. 필드 인잭션 (변경안됨) -> 비추
    private final MemberRepository memberRepository;  // final 넣는거 추천

    // 2. 생성자 인잭션 추천
//    @Autowired // 없어도 알아서 인잭션해준다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    // 3. 이 모든것을 해주는게 AllArgsConstructor
    // 4. RequiredArgsConstructor final있는 필드만 가지고 생성자를 만들어준다.

    /**
     * 회원 가입
      */
    @Transactional  // default == false
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();           // id 반환

    }

    // 중복 회원 검증 메서드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());  // member의 name을 unique 제약조건을 걸어주는것을 추천
        if (!findMembers.isEmpty()) {
             throw new IllegalArgumentException("이미 존재하는 회원입니당.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
