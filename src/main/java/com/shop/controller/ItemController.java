package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    //상품 관리 화면 이동 및 조회한 상품 데이터를 화면에 전달
    //한개 화면에 3개의 상품
    //페이지 번호는 0부터 시작

    //페이지가 있는 경우 , 번호가 없는 경우 두가지를 매핑
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto , @PathVariable("page") Optional<Integer> page , Model model){
        //페이지 객체 생성 첫번째는 시작 파라마터 두번째는 한번에 가져올 데이터 수
        //url 경로에 페이지 번호가 있으면 해당 페이지 조회, 페이지가 없으면 0 페이지 조회
        Pageable pageable = PageRequest.of(page.isPresent()? page.get(): 0, 3);
        //조회 조건과 페이징 정보를 파라마터로 넘겨서 Pager객체 반환
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto,pageable);
        model.addAttribute("items",items);//조회한 상품 데이터 및 페이징 정보 뷰에 전달
        model.addAttribute("itemSearchDto",itemSearchDto);//페이지 전환시 기존 검색조건을 유지한채 이동 하도록 뷰에 전달
        model.addAttribute("maxPage",5);//상품관리 메뉴 하단에 포여줄 최대 페이지 갯수
        return "item/itemMng";
    }

    @GetMapping(value = "admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId")  Long itemId , Model model){
        try{
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);//조회할 상품을 모델에 담아서 뷰에 전달
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){//상품이 존재 하지 않으면 에러메세지 호출후 상품 등록페이지 이동
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemFrom";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto , BindingResult bindingResult, @RequestParam("itemImgFile")
                             List<MultipartFile> itemImgFileList , Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상품은 필수 입력값입니다");
            return "item/itemForm";
        }
        try{ itemService.updateItem(itemFormDto,itemImgFileList);}
        catch (Exception e){
            model.addAttribute("errorMessage","상품수정중 에러가 발생했습니다");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDetail(Model model , @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
        model.addAttribute("item",itemFormDto);
        return "item/itemDetail";
    }

}
