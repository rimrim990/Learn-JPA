package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // Order 테이블에 있는 member 필드에 의해 매핑 - 읽기전용
    // 매핑된 값이 변경되어도 fk 가 변경되지 않음
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
