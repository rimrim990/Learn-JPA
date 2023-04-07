package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleRepository {

    private static EntityManager em;

    public List<SimpleOrderQueryDto> findOrderDtos() {
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.SimpleOrderQueryDto(o.id, o.orderDate, o.status)" +
                        " from Order o " +
                        "join o.member m " +
                        "join o.delivery d", SimpleOrderQueryDto.class)
                .getResultList();
    }
}
