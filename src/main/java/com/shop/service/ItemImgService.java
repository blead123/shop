package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;


    private final ItemImageRepository itemImageRepository;

    private final FileService fileService;

    public void  saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();//업로드 했던 상품이미지파일의 원래 이름
        String imgName = "";//실제 로컬에 저장된 파일명
        String imgUrl =  "";//저장할 상품 이미지를 불러올 경로

        //파일 업로드

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation , oriImgName , itemImgFile.getBytes());
            imgUrl = "/images/item/"+imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName,imgName,imgUrl);
        itemImageRepository.save(itemImg);


    }
}
