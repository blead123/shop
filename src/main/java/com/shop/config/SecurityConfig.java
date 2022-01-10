package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 체인이 포함->메소드 오버라이딩으로 보안설정 커스터 마이징
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    MemberService memberService;
    //http 요청에 대한 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))//로그아웃 url 설정
                .logoutSuccessUrl("/")
        ;

        http.authorizeRequests()//시큐리티 전체에 서블릿 리퀘스트 이용
                .mvcMatchers("/","/members/**","/item/**","/images/**").permitAll()//모든사용자가 인증없이 경로접근 가능
                .mvcMatchers("/admin/**").hasRole("ADMIN")//관리자 페이지는 관리자만 접근가능
                .anyRequest().authenticated();//나머지는 전부 인증을 요구

        //인증되지 않은 사용자 리소스 접근시 수행되는 핸들러 등록
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }
    //hash 함수로 비밀번호를 암호화해서 저장
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //인증매니저로 인증을 이룸
    //유저 디테일 서비스를 구현하는 개체로 멤버 서비스를 지정 및 비밀번호 암호화
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }



    //template 폴더의 static 파일은 무시
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
    }
}
