package com.shop.entity;
import com.shop.dto.ItemFormDto;
import  com.shop.entity.constant.ItemSellStatus;
import lombok.Builder;
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
public class Item extends BaseEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false , length = 50)
    private String itemName;

    @Column(nullable = false , name = "price")
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus isSold;

    public void updateItem(ItemFormDto itemFormDto ){
        this.itemName = itemFormDto.getItemName();
        this.price=itemFormDto.getPrice();
        this.stockNumber=itemFormDto.getStockNumber();
        this.description=itemFormDto.getDescription();
        this.isSold=itemFormDto.getIsSold();
    }


}
