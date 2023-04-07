package jpabasic.jpashop;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    // 기본키
    @Id
    // 자동 생성
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // 양방향 연관관계
    // 읽기전용 - fk 에 영향을 주지 않음
    @OneToMany(mappedBy = "member")
    public List<Order> orders = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }

    public Member() {

    }
}
