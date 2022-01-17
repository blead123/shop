package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    //널포인터익셉션을 피하기 위해 optional 사용
    @Override
    public Optional<String> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId=authentication.getName();//현재 로그인한 사용자의 정보를 호회 및 사용자이름을 등록자와 수정자로 설정
        }
        return Optional.of(userId);
    }
}
