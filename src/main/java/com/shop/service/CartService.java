package com.shop.service;

import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        //장바구니에 담을 상품 조회
        Item item = itemRepository.findById(cartItemDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원 엔티티 조회
        Member member = memberRepository.findByEmail(email);

        //현재 로그인한 회원의 장바구니 엔티티 조회
        Cart cart = cartRepository.findByMemberId(member.getId());

        //장바구니가 없으면 새로 생성 후 카트 저장
        if (cart == null) {
            cart = Cart.makeCart(member);
            cartRepository.save(cart);
        }

        //현재 상품이 이미 장바구니에 들어있는지 조회
        CartItem saveCartItem = cartItemRepository.findByCartIdAndMemberId(cart.getId(), item.getId());

        //이미 있는 상품일 경우 수량을 추가한 만큼 더해줌
        //없으면 새로 만들고 저장하자
        if (saveCartItem != null) {
            saveCartItem.addCartItem(cartItemDto.getCount());
            return saveCartItem.getId();
        } else {
            CartItem cartItem = CartItem.makeCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }


}
