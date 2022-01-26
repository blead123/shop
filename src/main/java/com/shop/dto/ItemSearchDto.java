package com.shop.dto;

import com.shop.entity.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//상품 조회 조건을 가진 dto
public class ItemSearchDto {
    private String searchDateType;//현재 시간과 상품 등록일을 비교해서 상품 데이터를 조회

    private ItemSellStatus searchSellStatus;//상품 판매 상태로 조회

    private String searchBy; // 상품을 어떤 유형으로 조사할지 조회-->상품명이나 상품등록자 아이디디

    private String searchQuery=""; // 조회할 검색어 저장 변수 serachBy itemNAEM->상품명 기준 CRREATEDbY 상품 등록자 아이디 기준검색
}
