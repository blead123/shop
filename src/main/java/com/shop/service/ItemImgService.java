package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityExistsException;


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

    public void updateImg(Long itemImgId , MultipartFile itemImgFile) throws Exception{
        //상품 이미지를 수정한 후 상품이미지 업데이트
        //상품 이미지 아이디를 이요해 저장했더 상품 이미지 엔티티 조회
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImageRepository.findById(itemImgId).orElseThrow(EntityExistsException::new);
           if(!StringUtils.isEmpty(savedItemImg.getImgName())){//기존 등록 상품이미지가 있을경우 해당 파일 삭제
               fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
           }
           String oriImgName = itemImgFile.getOriginalFilename();
           //업데이트한 상품이미지 업로드
           String imgName = fileService.uploadFile(itemImgLocation , oriImgName , itemImgFile.getBytes());
           String imgUrl = "/images/item"+imgName;
           //변경 정보 저장
           savedItemImg.updateItemImg(oriImgName , imgName , imgUrl);
        }


    }
}
