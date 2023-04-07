package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "1111");

            Book book = createBook("JPA1 BOOK", 10000);
            Book book2 = createBook("JPA2 BOOK", 20000);

            OrderItem orderItem1 = OrderItem.createItemOrder(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createItemOrder(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "경기", "1", "1111");

            Book book = createBook("SPRING1 BOOK", 10000);
            Book book2 = createBook("SPRING2 BOOK", 20000);

            OrderItem orderItem1 = OrderItem.createItemOrder(book, 20000, 1);
            OrderItem orderItem2 = OrderItem.createItemOrder(book2, 25000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Book createBook(String JPA1_BOOK, int price) {
            Book book = new Book();
            book.setName(JPA1_BOOK);
            book.setPrice(price);
            book.setStockQuantity(100);
            em.persist(book);
            return book;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(name, city, zipcode));
            em.persist(member);
            return member;
        }
    }

}
