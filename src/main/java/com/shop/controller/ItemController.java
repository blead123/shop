package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

//ItemFormDto를 model 객체에 담아서 뷰로 전달
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }
    //상품등록 url
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto , BindingResult bindingResult ,
                          Model model , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        //상품값이 없다면 다시 상품등록 페이지
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        //첫번째 이미지가 없으면 상품등록 페이지로 전환
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상품이미지는 필수 입니다");
            return "item/itemForm";
        }
        //상품 저장 로직 호출 , 상품정보와 상품정보이미지를 담고있는 리스트 전달
        try{
            itemService.saveItem(itemFormDto , itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","사움등록중 에러가 발생했습니다");
            return "item/itemForm";
        }
        //저장 성공시 메인페이지 이동
        return "redirect:/";
    }
}
