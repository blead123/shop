package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
@Getter
@Setter
public class OrderDto {
    @NotNull(message = "상품아이디는 필수 값 입니다")
    private Long itemId;

    @Min(value = 1 , message = "최소주문 수량은 1개 입니다")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다")
    private int count;
}
