package com.shop.entity;

import com.shop.entity.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
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
    //주문 데이터 생성 메소드
    public Order createOrder(){
        Order order = new Order();
        for(int i =1; i<=10;i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }
    @Test
    @DisplayName("고아객체 제거 테스팅")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(1);
        em.flush();
    }

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
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("지연로딩")
    public void delayedLoadingTest(){
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class :"+orderItem.getOrder().getClass());//실제 객체 대신 프록시 객체
        System.out.println("----------------------------------------------");
        orderItem.getOrder().getOrderDate();//주문일을 조회할때 SELECT문 실행
        System.out.println("----------------------------------------------");

    }





}