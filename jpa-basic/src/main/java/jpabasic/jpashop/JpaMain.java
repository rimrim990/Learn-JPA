package jpabasic.jpashop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // 하나만 만들어야 한다
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션 단위로 생성 - 사용자 요청마다 생성, 쓰레드 간에 공유 X
        EntityManager em = emf.createEntityManager();

        // 데이터베이스를 조작하는 일은 트랜잭션 안에서 수행되어야 한다
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            // 데이터베이스에서 조회 후 1차 캐시에 저장한다
            Member findMember = em.find(Member.class, 1L);
            // 1차 캐시에서 가져온다
            Member findMember2 = em.find(Member.class, 1L);

            Member member = new Member("Hello~");
            // SQL 쓰기 지연 저장소
            em.persist(member);

            // 변경 사항 강제 반영
            // em.flush();

            // 영속 엔티티의 동일성 보장
            System.out.println(findMember == findMember2);

            // JPQL - 객체를 대상으로 검색 쿼리 생성
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(5)
                    .getResultList();

            // 커밋하는 순간 데이터베이스에 INSERT SQL 을 보낸다 (버퍼링)
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
