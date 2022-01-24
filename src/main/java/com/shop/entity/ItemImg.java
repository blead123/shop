package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "item_img")
public class ItemImg extends BaseEntity {
    @Id
    @Column(name = "item_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repimgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    //원본 이미지 파일명 , 업데이트 이미지 파일명 , 이미지경로를 파라미터로 입력받아서 이미지 정보 업데이트
    public void updateItemImg(String oriImgName, String imgName , String imgUrl){
        this.oriImgName=oriImgName;
        this.imgName=imgName;
        this.imgUrl=imgUrl;
    }

}
