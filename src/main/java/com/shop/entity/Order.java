package com.shop.entity;

import com.shop.entity.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne//한명의 회원은 여러번 주문이 가능 일대 다 관계로 매핑
    @JoinColumn(name = "member_id")//외래키 참조
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //왜래키가 order_item테이블에 존재--> 연관관계의 주인은 orderItem 엔티티
    //속성이 order 인 이유는 OrderItem에 있는 Order에 의해서 관리되는 의미로 해석
    //-->연관관계의 주인을 mappedBy의 값으로 세팅
    //부모 엔티티의 영속성 상태변화를 자식 엔티티에 모두 전이
    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    //하나의 주문은 여러개의 상품값-->리스트 타입
    private List<OrderItem> orderItems = new ArrayList<>();

    //주문상품 담기기
   public void addOrderItem(OrderItem orderItem){
      //양방향 참조 관계
       orderItems.add(orderItem);
       orderItem.setOrder(this);
   }
   //주문 생성
   public static Order createOrder(Member member , List<OrderItem> orderItemList){
       Order order = new Order();
       order.setMember(member);

       for(OrderItem orderItem : orderItemList){
           order.addOrderItem(orderItem);
       }
       order.setOrderStatus(OrderStatus.ORDER);
       order.setOrderDate(LocalDateTime.now());
       return order;
   }

   //전체가격 계산
   public int getTotalPrice(){
       int totalPrice = 0;
       for(OrderItem orderItem : orderItems){
           totalPrice=totalPrice+orderItem.getTotalPrice();
       }
       return totalPrice;
   }
   //주문 취소시 주문수량을 상품의 재고에 더해주는 로직 주문 상태를 취소로 만드는 로직
    public void cancelOrder(){
       this.orderStatus=OrderStatus.CANCEL;

       for(OrderItem orderItem : orderItems){
           orderItem.cancel();
       }
    }
}
