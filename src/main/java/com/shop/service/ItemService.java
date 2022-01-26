package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImageRepository;
import com.shop.repository.ItemRepository;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    //메인 페이지 보여줄 상품 조회 메소드
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto , Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto , pageable);
    }

    @Transactional(readOnly = true)//더티체킹을 안하므로 성능향상
    public ItemFormDto getItemDetail(Long itemId){
        //해당이미지를 조회
        List<ItemImg> itemImgList = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){ // 조회한 아이템이미지 엔티티를 아이템 이미지 디티오 객체로 만들어서 리스트 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    //상품 업데이트

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품수정
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //상품 이미지 아이디 리스트 조회
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateImg(itemImgIds.get(i),itemImgFileList.get(i));
        }
        return item.getId();
    }
}
