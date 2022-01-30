package com.shop.dto;

import com.shop.entity.Order;
import com.shop.entity.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//주문내역 DTO
@Getter
@Setter
public class OrderHisDto {

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    public OrderHisDto(Order order){
        this.orderId = order.getId();
        //order 객체를 파라미터로 받아 멤버 변수 세팅
        //주문 날짜의 경우 "yyyy-mm--dd hh:mm" 타입이로 전달
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
    // 객체를 주문상품 리스트에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }

}
