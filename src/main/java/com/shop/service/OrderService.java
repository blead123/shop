package com.shop.service;


import com.shop.dto.OrderDto;
import com.shop.dto.OrderHisDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImageRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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
    private final ItemImageRepository itemImageRepository;

    public Long order(OrderDto orderDto ,String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item , orderDto.getCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member , orderItemList);

        orderRepository.save(order);

        return order.getId();
    }
    @Transactional(readOnly = true)
    public Page<OrderHisDto> getOrderList(String email , Pageable pageable){
        //유저의 아이다와 페이징 조건을 이용해 주문 목록 조회
        List<Order> orders = orderRepository.findOrders(email,pageable);
        //유저의 주문 총 갯수
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHisDto> orderHisDtos = new ArrayList<>();
        //주문 리스트를 순회하면서 구매 이력 페이지에 전달할 Dto 생성
        for (Order order : orders){
            OrderHisDto orderHisDto = new OrderHisDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems){
                //주문 상품의 대표 이미지 조회
                ItemImg itemImg = itemImageRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(),"Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem , itemImg.getImgUrl());
                orderHisDto.addOrderItemDto(orderItemDto);
            }
            orderHisDtos.add(orderHisDto);
        }
        //페이지 구현 객체
        return new PageImpl<OrderHisDto>(orderHisDtos , pageable , totalCount);
    }
    //주문 취소 로직
    public void cancelOrder(Long orderId){
        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
    //현재 주문을취소한 사용자가 같은지 체크
    public boolean validateOrder(Long orderId ,String email ){
        Member currentMember = memberRepository.findByEmail(email);//현재 멤버
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);//주문번호로 주문 찾기
        Member savedMember = order.getMember();//주문 멤버

        if(!StringUtils.equals(currentMember.getEmail(),savedMember.getEmail()))
            return false;
        else return true;
    }
}
