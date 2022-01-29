package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

//새로고침x--->비동기 방식
@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    //스프링에서 비동기 처리
    // @RequestBody , @ResponseBody 사용
    // @RequestBody --> HTTP 요청의 본문 Body에 담긴 내용 자바 객체로 전달
    // @ResponseBody --> 자바 객체를 데이터에 담아 바인딩시 에러가 있는 지 검사
    @PostMapping(value = "/order")
    //주문 정보를 받는 orderDto 객체에 데이터 바인딩 에러가 있는지 검사
    public @ResponseBody ResponseEntity order(@ResponseBody @Valid OrderDto orderDto , BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();

            for( FieldError fieldError : fieldErrorList){
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            //요청 정보를 ResponseEntity객체에 반환
            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        //현대 로그인 유저의 정보를 얻기 위헤 @Controller 어노테이션이 선언된 클래스에서 메소드 인자로 prinicipla 객체로 너겨주면
        //해당 객체에 직접 접근 가능
        //princial 객체에서 현재 로그인한 회원의 이메일 정보 조회
        String email = principal.getName();
        Long orderId;
        try{
            //주문 로직 호출
            orderId = orderService.order(orderDto , email);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        //호출 성공!
        return new ResponseEntity<Long>(orderId , HttpStatus.OK);
    }
}
