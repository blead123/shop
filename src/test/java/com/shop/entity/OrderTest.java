package com.shop.entity;

import com.shop.entity.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemName("test item");
        item.setPrice(10000);
        item.setDescription("test item");
        item.setIsSold(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegisteredTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    public Order createOrder(){
        Order order =new Order();
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTEST(){
        Order order = new Order();

        for (int i =1; i<=10;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem =new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrder(order);
            //영속성 콘텍스트에 저장되지 않은 orderitem 엔티티를 order엔티티에 담아줌
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);//db 반영
       em.clear();//영속성 콘텍스트 상태 초기화

        Order savedOrder= orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(10,savedOrder.getOrderItems().size());//실제 db에 3개가 저장됫는지 확인
    }
}