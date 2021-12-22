package com.shop.entity;
import  com.shop.entity.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false , length = 50)
    private String itemName;

    @Column(nullable = false , name = "price")
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private int description;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus isSold;

    private LocalDateTime registeredTime;

    private LocalDateTime updateTime;


}
