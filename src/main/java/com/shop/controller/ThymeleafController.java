package com.shop.controller;

import com.shop.entity.Item;
import com.shop.entity.constant.ItemSellStatus;
import lombok.Builder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shop.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafController {
    //ex01페이지로 이동
    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data","타임리프 예제");
       return "thymeleafEx/thymeleafEx01";
    }
    //ex02 페이지로 이동
    //itemDto 객체를 생성후 모델에 데이터를 담아서 뷰에 전달
    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model){
    ItemDto itemDto = new ItemDto();
    itemDto.setItemName("테스트 상품");
    itemDto.setPrice(1000);
    itemDto.setDescription("테스트 상품 상세 설명");
    itemDto.setRegTime(LocalDateTime.now());

    model.addAttribute("itemDto",itemDto);
    return "thymeleafEx/thymeleafEx02";
    }
    //ex03페이지로 이동
    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1;i<=10;i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName("테스트상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setDescription("테스트상품"+i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute(itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i =1 ; i<=10 ; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName("테스트상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setDescription("테스트상품"+i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i =1 ; i<=10 ; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName("테스트상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setDescription("테스트상품"+i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    public String thymeleafExample06(Model model){
        List<ItemDto>  itemDtoList= new ArrayList<>();
        for(int i =1 ;i<=10 ; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName("테스트 상품"+i);
            itemDto.setPrice(10000+i);
            itemDto.setDescription("테스트 상품 상세설명"+i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    public String thymeleafExample07(String param1 , String param2 , Model model){
        model.addAttribute("param1",param1);
        model.addAttribute("param2",param2);
        return "thymeleafEx/thymeleafEx07";
    }

    @GetMapping(value ="/ex08")
    public String thymeleafExample08(){
        return "thymeleafEx/thymeleafEx08";
    }
}
