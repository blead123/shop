package com.shop.controller;

import com.shop.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//ItemFormDto를 model 객체에 담아서 뷰로 전달
@Controller
public class ItemController {
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }
}
