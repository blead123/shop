package com.shop.dto;

import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
//상품의 아이디와 수량을 전달받을 CartItemDto 클래스
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수 값 입니다")
    private Long cartItemId;

    @Min(value =1 , message="상품은 반드시 한개 이상 담아야 합니다")
    private int count;
}
