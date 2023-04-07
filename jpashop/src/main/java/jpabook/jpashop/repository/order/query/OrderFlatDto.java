package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Address;

import java.util.ArrayList;
import java.util.List;

public class OrderFlatDto {
    private Long orderId;
    private String name;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
