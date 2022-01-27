package com.shop.entity;

import com.shop.dto.MemberFormDto;
import com.shop.entity.constant.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true) // 이메일로 구분
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING) // 이넘타입시 순서가 저장되는데 순서 문제를 해결하기 위해 String 저장
    private Role role;

    // 멤버 엔티티 생성 매소드->코드가 변경되더라도 한군데만수정
    public static Member createMember(MemberFormDto memberFormDto , PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setAddress(memberFormDto.getAddress());
        member.setEmail(memberFormDto.getEmail());
        //비밀번호 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }


}
