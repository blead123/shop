package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//사용자 정의 인터페이스
public interface ItemRepositoryCustom {
    //상품 조회 조건을 담고 있는 itemSerachDto 객체와 페이징 정보를 담고있는 pageale 객체를 파라미터로 받음
    //반환 데이터로 Page<ITEM> 객체
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto , Pageable pageable);
}
