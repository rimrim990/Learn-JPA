package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.SimpleOrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    // 동적으로 생성해야 한다 !
    public List<Order> findAll(OrderSearch orderSearch) {
        // jpql
        return em.createQuery("select o from Order o join o.member m " +
                        "where o.status =: status " +
                        "and m.name =: name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000건
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        // 패치 조인
        return em.createQuery("select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        /**
         * Order -> 2개
        * OrderItem -> 4개
        * 최종 생성 결과 4개 -> (1, 3), (1,4), (2,11), (2, 13)
        */
        // distinct - DB distinct + JPA 에서 엔티티 중복 제거하여 컬렉션 만들어준다
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member" +
                                " join fetch o.delivery" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                // collection fetch -> applying in memory !
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int  offset, int limit) {
        return em.createQuery("select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
