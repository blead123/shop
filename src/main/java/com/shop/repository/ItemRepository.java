package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item , Long> , QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    @Query("select i from Item i where i.description like " + "%:description% order by i.price desc")
    List<Item> findByDescription(@Param("description") String description );

    List<Item> findByItemName(String itemName);

    List<Item> findByItemNameOrDescription(String itemName, String description);

    List<Item> findByPriceLessThan(int price);

    List<Item> findByPriceLessThanOrderByPriceDesc(int price);

}
