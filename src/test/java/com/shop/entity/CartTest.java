package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager em;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@google.com");
        memberFormDto.setName("tester");
        memberFormDto.setPassword("1234");
        memberFormDto.setAddress("여수");
        return Member.createMember(memberFormDto,passwordEncoder);
    }

    @Test
    @DisplayName("cart test")
    public void findCartMemberTest(){
        Member member = createMember();
        memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();//데이터 db반영
        em.clear();//영속성 콘텍스트에 데이터가 없으면 디비 조회 , 실제로 디비에서 장바구니 엔티티를 가지고 올때 회원 엔티티도 같이 가지고 오는지 체크하기 위해 영속성 컨텍스트를 비움

        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId() , member.getId());//처음저자안 member 엔티티의 id == savecart id

    }

}