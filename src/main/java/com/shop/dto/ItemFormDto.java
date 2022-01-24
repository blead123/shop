package com.shop.dto;

import com.shop.entity.Item;
import com.shop.entity.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    public Long id;

    @NotBlank(message="상품명은 필수입니다")
    private String itemName;

    @NotNull(message = "가격은 필수 입니다")
    private Integer price;

    @NotNull(message = "이름은 필수 입력사항입니다")
    private String description;

    @NotNull(message = "재고는 필수 입력 값입니다")
    private Integer stockNumber;

    private ItemSellStatus isSold;

    //상품 이미지 저장 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    //상품 이미지의 아이디를 저장하는 리스트
    private List<Long> itemImgIds = new ArrayList<>();

    //modelmapper 선언
    private static ModelMapper modelMapper = new ModelMapper();

    //modelMapper로 엔티티와 객체와 DTO객체 간의 데이터를 복사후 복사한 객체를 반환
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item , ItemFormDto.class);
    }

}
