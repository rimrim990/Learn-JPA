package jpabasic.jpashop;

import javax.persistence.*;

// 슈퍼 타입
@Entity
// 싱글 테이블 전략 - 하나의 테이블에 모든 칼럼 생성
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 서브 타입 구별을 위한 값 - dtype
@DiscriminatorColumn
// 단독 사용하지 않으므로 추상 클래스로 생성
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
