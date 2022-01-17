package com.shop.entity;

import com.shop.repository.MemberRepository;
import lombok.With;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.lang.management.MemoryManagerMXBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties.")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("ADUTIDNG TEST")
    @WithMockUser(username = "tester" , roles = "user")
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);


        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);

        System.out.println("등록일"+member.getRegisteredTime());
        System.out.println("수정일"+member.getUpdateTime());
        System.out.println("등록자"+member.getCreatedBy());
        System.out.println("수정자"+member.getModifiedBy());

    }

}