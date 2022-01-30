package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHisDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.Ingres9Dialect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto , BindingResult bindingResult ,
                                              Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        Long orderId;
        try{
            orderId = orderService.order(orderDto , email);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId , HttpStatus.OK);
    }
    //구매 이력 조회
    @GetMapping(value = {"/orders","orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page , Principal principal , Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ?page.get() : 0,4);
        //현재 로그인한 회원은 이메일과 페이징 객체를 파라메터로 전달
        //화면에 전달한 주문 목록 데이터를 리턴값을 받음
        Page<OrderHisDto> orderHisDtoList = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders",orderHisDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist";
    }
}
