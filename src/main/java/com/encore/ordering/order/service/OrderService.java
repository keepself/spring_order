package com.encore.ordering.order.service;

import com.encore.ordering.item.domain.Item;
import com.encore.ordering.item.respository.ItemRepository;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.repository.MemberRepository;
import com.encore.ordering.order.domain.OrderStatus;
import com.encore.ordering.order.domain.Ordering;
import com.encore.ordering.order.dto.OrderRequest;
import com.encore.ordering.order.dto.OrderResponse;
import com.encore.ordering.order.repository.OrderRepository;
import com.encore.ordering.order_item.domain.OrderItem;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.net.Authenticator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    public List<OrderResponse> findMyOrders;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }

    public Ordering create(OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("not found email"));

        Ordering ordering = Ordering.builder().member(member).build();
//    Ordering 객체가 생성될때 OrderingItem객체도 함께 생성 :cascading
        for (OrderRequest.OrderReqItemDto dto : orderRequest.getOrderReqItemDtos()) {
            Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new EntityNotFoundException("아이템을 찾을수 없습니다♥"));
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .quantity(dto.getCount())
                    .ordering(ordering)
                    .build();
            ordering.getOrderItems().add(orderItem);
            if (item.getStockQuantity() - dto.getCount() < 0) {
                throw new IllegalArgumentException("재고가 부족합니다.");
            }
            orderItem.getItem().updateStockQuantity(item.getStockQuantity() - dto.getCount());
        }
        return orderRepository.save(ordering);

    }

    public Ordering cancel(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Ordering ordering = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found order"));
        if (!ordering.getMember().getEmail().equals(email) && !authentication.getAuthorities().contains((new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        if (ordering.getOrderStatus() == OrderStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 주문입니다.");
        }
        ordering.cancelOrder();
        for (OrderItem orderItem : ordering.getOrderItems()) {
            orderItem.getItem().updateStockQuantity(
                    orderItem.getItem().getStockQuantity() + orderItem.getQuantity());
        }
        return ordering;
    }

    public List<OrderResponse> findAll() {
        List<Ordering> orderings = orderRepository.findAll();
        return orderings.stream().map(o->OrderResponse.toDto(o)).collect(Collectors.toList());


    }

    public List<OrderResponse> findByMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
        List<Ordering> orderings = member.getOrderings();
        return orderings.stream().map(o->OrderResponse.toDto(o)).collect(Collectors.toList());

    }

    public  List<OrderResponse> findMyOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException());
        List<Ordering> orderings = member.getOrderings();
        return orderings.stream().map(o->OrderResponse.toDto(o)).collect(Collectors.toList());

    }
}
