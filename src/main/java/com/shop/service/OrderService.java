package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto , String email){
        //주문 상품 조회
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        //이메일로 회원정보 조회
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        //주문 상품과 갯수로 주문상품 엔티티 생성
        OrderItem orderItem = OrderItem.createOrderItem(item , orderDto.getCount());
        orderItemList.add(orderItem);
        //회원정보와 주문할 상품 리스트 정보로 주문 엔티티 생성
        Order order = Order.createOrder(member , orderItemList);
        orderRepository.save(order);
        //저장
        return order.getId();
    }
}
