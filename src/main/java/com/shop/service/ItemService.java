package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImageRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

//상품 등록
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImageRepository itemImageRepository;

    public Long saveItem(ItemFormDto itemFormDto , List<MultipartFile> itemImgFileList) throws Exception{
        //상품 등록
        Item item = itemFormDto.createItem();//상품등록 폼으로부터 받은 데이터를 이용해 item 객체 생성
        itemRepository.save(item);//상품 데이터 저장

        //이미지 등록
        for(int i =0 ;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0)//첫번째일 경우 대표이미지설정
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));//이미지 정보 저장
        }
        return item.getId();
   }

   //상품 조회 조건과 페이지 정보를 파라미터로 받아서 상품 데이터 조회
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto , Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto , pageable);
    }
}
