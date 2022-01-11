package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter
@Setter
@ToString
public class Cart {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne // 1:1매핑
    @JoinColumn(name = "member_id")//매핑할 외래키 설정 name에는 외래키의 이름설정 jpa가 알아서 만들기도 하지만 직접하는게 좋음
    private Member member;
}