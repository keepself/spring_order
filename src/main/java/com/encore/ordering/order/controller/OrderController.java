package com.encore.ordering.order.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderRequest;
import com.encore.ordering.order.dto.OrderResponse;
import com.encore.ordering.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {
private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponse> orderCreate(@RequestBody OrderRequest orderRequest){
        Ordering odering = orderService.create(orderRequest);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED,"오더가 성공했습니다",odering.getId()),HttpStatus.CREATED);

    }
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponse> orderCancel(@PathVariable Long id){
        Ordering odering = orderService.cancel(id);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,"order succesfully canceld",odering.getId()),HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<OrderResponse> orderList(){

        return orderService.findAll();

    }
}
