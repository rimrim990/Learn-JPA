package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// 스프링 부트 컴포넌트 스캔
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // 스프링 부트 - 엔티티 매니저 의존성 주입
    // @PersistenceContext
    private final EntityManager em;

    public void save(Member member) {
        // 영속성 컨텍스트에 멤버 객체 삽입
        // 트랜잭션이 커밋되는 시점에 DB 에 반영 - insert 쿼리 보냄
        em.persist(member);
    }

    public Member findOne(Long id) {
        // 타입, pk
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // jpql - 테이블이 아닌 객체를 대상으로 쿼리 - SQL 로 번역됨
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
