package com.shop.dto;
//dto-->데이터 전송용 객체 , 엔티티(데이터가 담김것)을 그대로 전송하는것 보다 더 좋음
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ItemDto {
    private Long id;

    private int price;

    private String itemName;

    private String description;

    private String isSold;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
