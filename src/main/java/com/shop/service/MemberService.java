package com.shop.service;
//비즈니스 로직 담기

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional //디비 트랜잭션 과같은 기능
@RequiredArgsConstructor // final이나 non null 이 붙은 필드에 생성자 생성.빈에 생성자가 하나 이고 파라미터타입이 빈으로 등록이 되면 autowired로 의존성 주입
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }
    //중복 회원 구분
    public void validateDuplicateMember(Member member){
        Member findMember =memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //userDetail 을 구현하는 User 객체 반환
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


}
