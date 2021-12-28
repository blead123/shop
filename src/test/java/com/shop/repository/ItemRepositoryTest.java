package com.shop.repository;

import com.shop.entity.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.shop.entity.QItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")

class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("test")

    public void createItemTest(){
    Item item = new Item();
    item.setItemName("테스트상품");
    item.setPrice(10000);
    item.setDescription("테스트 상세 설명");
    item.setIsSold(ItemSellStatus.SELL);
    item.setStockNumber(100);
    item.setRegisteredTime(LocalDateTime.now());
    item.setUpdateTime(LocalDateTime.now());
    Item savedItem = itemRepository.save(item);
    System.out.println(savedItem.toString());
    }

    public void createItemList(){
     for(int i=1;i<=10;i++){
         Item item = new Item();
         item.setItemName("테스트상품"+i);
         item.setPrice(10000+i);
         item.setDescription("테스트상세설명"+i);
         item.setIsSold(ItemSellStatus.SELL);
         item.setStockNumber(100);
         item.setRegisteredTime(LocalDateTime.now());
         item.setUpdateTime(LocalDateTime.now());
         Item savedItem = itemRepository.save(item);
          }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("테스트상품1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명 or 상세설명 찾기 테스트")
    public void findByItemNameORDescriptionTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNameOrDescription("테스트상품1","테스트상세설명1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 less than test")
    public void findByPriceLessThanTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item:itemList){
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("가격 소팅 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10001);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("쿼리 테스트")
    public void findByDescriptionTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByDescription("테스트 상품 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("querydsl 조회테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Item> query =queryFactory.selectFrom(qItem)
                .where(qItem.isSold.eq(ItemSellStatus.SELL))
                .where(qItem.description.like("%"+"테스트 상세 설명"+"%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println(item.toString());
        }

    }

    public void createItemList2(){
        for (int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemName("테스트상품"+i);
            item.setPrice(10000+i);
            item.setDescription("테스트상품 상세 설명");
            item.setIsSold(ItemSellStatus.SOLDOUT);
            item.setStockNumber(0);
            item.setRegisteredTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 qureldsl test 2")
    public void queryDslTest2(){
        this.createItemList();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        int price =3000;
        String description = "테스트상품 상세 설명";
        String isSold = "SELL";

        booleanBuilder.and(item.description.like("%"+description+"%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(item.isSold,ItemSellStatus.SELL)){
            booleanBuilder.and(item.isSold.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,10);
        Page<Item> itemPagingResult=
                itemRepository.findAll(booleanBuilder,pageable);
        System.out.println("total elements :"+itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }


}