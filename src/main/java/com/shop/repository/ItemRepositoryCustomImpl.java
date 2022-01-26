package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.constant.ItemSellStatus;
import com.querydsl.core.types.dsl.BooleanExpression;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

//구현체
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    private JPAQueryFactory queryFactory;//동적 쿼리 생성

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);//생성자로 엔티티 매니저 주입
    }

    //상품 판매 조건이 전체-->null리턴
    //상품 판매 상태 조건이 null이 아나리 판매중 품절 상태라면 해당 조건의 상품만 조회
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus ==
                null ? null : QItem.item.isSold.eq(searchSellStatus);
    }
    //서치데이트타입 값에 따라 데이트 타임값을 이전 시간으로 값으로 세팅 , 해당 시간이후로 등록된 상품만 조회
    //프론트에서 한달전 상품만 보기 설정시 dateTime 값을 1m로 설정후 최근 한달동안 등록된 상품만 조회
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all",searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w",searchDateType)){
            dateTime=dateTime.minusWeeks(7);
        } else if(StringUtils.equals("1m",searchDateType)){
            dateTime=dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m",searchDateType)){
            dateTime=dateTime.minusMonths(6);
        }
        return QItem.item.registeredTime.after(dateTime);
    }
    //서치 파이의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값 반환
    private BooleanExpression searchByLike(String searchBy , String searchQuery){
        if(StringUtils.equals("itemName",searchBy)){
            return QItem.item.itemName.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchBy+"%");
        }
        return null;
    }
    //쿼리 팩토리를 이용한 쿼리 생성

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto , Pageable pageable){
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())//데이터를 가지고올 시작 인덱스
                .limit(pageable.getPageSize())//한번에 가저올 데이터 갯수
                .fetchResults();//조회한 리스트 및 전체 개수를 포함하는 쿼리 리슐트를 반환 사움 데이터 리스트 조회 및 상품 데이터 전체 갯수를 조회하는 두번의 쿼리 실행

        List<Item> content = results.getResults();
        Long total = results.getTotal();
        return new PageImpl<>(content , pageable , total);

    }
}
