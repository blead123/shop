package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@Getter
@Setter

public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne//하나의 상품은 여러 주문 상품으로 들어갈수 있음
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne//한번의 주문은 여러개의 상품 주문 가능
    @JoinColumn(name="order_id")
    private Order order;

    private int price;//가격

    private int count;//갯수

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
