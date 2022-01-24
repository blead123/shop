package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.entity.constant.ItemSellStatus;
import com.shop.repository.ItemImageRepository;
import com.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImageRepository itemImageRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFilesList = new ArrayList<>();

        for(int i =0 ; i<5; i++){
            String path="C:/shop/item/";
            String imageName="image"+i+".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path,imageName,"image/jpg",new byte[]{1,2,3,4});
            multipartFilesList.add(multipartFile);
        }
        return multipartFilesList;
    }

    @Test
    @DisplayName("상품등록 테스트")
    @WithMockUser(username = "admin" , roles = "ADMIN")
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("테스트 상품");
        itemFormDto.setIsSold(ItemSellStatus.SELL);
        itemFormDto.setDescription("테트스 상품입니다");
        itemFormDto.setPrice(100);
        itemFormDto.setStockNumber(1000);

        List<MultipartFile> multipartFilesList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto,multipartFilesList);

        List<ItemImg> itemImgList = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemName(),item.getItemName());
        assertEquals(itemFormDto.getIsSold() , item.getIsSold());
        assertEquals(itemFormDto.getDescription() , item.getDescription());
        assertEquals(itemFormDto.getPrice() , item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFilesList.get(0).getOriginalFilename(),itemImgList.get(0).getOriImgName());
    }

}