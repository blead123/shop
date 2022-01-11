package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne//하나의 장바구니 여러개의 아이템
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne//하나의 상품은 여러개의 장바구니에 담길 수 있음
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;//같은 상품을 몇개 담을 것인지 설정

}
