package jpabasic.jpashop;

import javax.persistence.*;

@Entity
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 연관관계 설정
    @ManyToOne
    // fk
    @JoinColumn(name = "member_id")
    private Member member;

    private int count;
    private int name;

    // Original 타입 X - 순서는 변경될 수 있음
    @Enumerated(value = EnumType.STRING)
    private OrderState orderState;

    // 연관관계 편의 메서드
    public void addMember(Member member) {
        this.member = member;
        member.orders.add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
