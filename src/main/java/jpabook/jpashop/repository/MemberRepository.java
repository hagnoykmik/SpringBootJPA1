package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository  // Component 스캔의 대상이 되서 스프링 빈으로 등록
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext            // 1. 스프링 엔티티매니저를 주입
    private final EntityManager em;  // 2. final 추가

    // member 저장
    public void save(Member member) {
        em.persist(member);             // 영속성 컨텍스트에 member 객체를 넣는다. 트랜잭션 commit 시점에 DB에 반영.
    }

    // member 단건 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);                                      // member를 찾아서 반환
//        Member member = em.find(Member.class, id);
//        return member; 를 합친것
    }

    // member list 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)  // 반환 타입 - Member.class
                .getResultList();

//        List<Member> result = em.createQuery("select m from Member m", Member.class)
//                .getResultList();
//        return result;

    }

    // member 이름으로 조회
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
