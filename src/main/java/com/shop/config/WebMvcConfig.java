package com.shop.config;
//webMvcConfigurer 를 구현하는 클래스
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")//설정에 있는 프로퍼티 값 읽어오기
    String uploadPath;

    //로컬 컴퓨터에 있는 파일 찾는 위치
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("**/images/**")//upLoadPath에 설정한 폴더를 기준으로 파일을 읽어옴
                .addResourceLocations(uploadPath);//루트 경로 설정
    }

}
