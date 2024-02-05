package com.encore.ordering.order.dto;

import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order_item.domain.OrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String memberEmail;
    private String orderStatus;
    private List<OrderResItemDto> orderResItemDtos;

    @Data
    public static class OrderResItemDto {
        private Long id;
        private String itemName;
        private int count;
    }

    public static OrderResponse toDto(Ordering ordering) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(ordering.getId());
        orderResponse.setMemberEmail(ordering.getMember().getEmail());
        orderResponse.setOrderStatus(ordering.getOrderStatus().toString());
        List<OrderResponse.OrderResItemDto> orderResItemDtos = new ArrayList<>();
        for (OrderItem orderItem : ordering.getOrderItems()) {
            OrderResponse.OrderResItemDto dto = new OrderResponse.OrderResItemDto();
            dto.setId(orderItem.getId());
            dto.setItemName(orderItem.getItem().getName());
            dto.setCount(orderItem.getQuantity());
            orderResItemDtos.add(dto);
        }
        orderResponse.setOrderResItemDtos(orderResItemDtos);

        return orderResponse;
    }
}
