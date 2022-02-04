package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//하나의 장바구니 여러개의 아이템
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)//하나의 상품은 여러개의 장바구니에 담길 수 있음
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;//같은 상품을 몇개 담을 것인지 설정

    //장바구니에 담을 상품 엔티티를 생성
    public static CartItem makeCartItem(Cart cart , Item item , int count){
        CartItem cartItem = new CartItem();
        //카트 생성
        cartItem.setCart(cart);
        //생성후 아이템을 담기
        cartItem.setItem(item);
        //갯수 담기
        cartItem.setCount(count);

        return cartItem;
    }
    //장바구니에 담을 수량을 증가
    //해당 상푸을 추가로 카트에 담음
    public void addCartItem(int count){
        this.count+=count;
    }
}
