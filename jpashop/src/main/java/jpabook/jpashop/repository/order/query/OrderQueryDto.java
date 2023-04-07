package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private Address address;

    private List<OrderItemQueryDto> orderItems = new ArrayList<>();

    public Long getOrderId() {
        return orderId;
    }

    public OrderQueryDto(Long orderId, String name, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.address = address;
    }
}
