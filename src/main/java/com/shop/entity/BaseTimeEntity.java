package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})//auditing 적용
//공통매핑정보가 필요할 시 사용하는 어노테이션 부모클래스를 상속받는 자식 클래스에 매핑정보만 제공
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {
    @CreatedDate//엔티티가 생성되어 저장되는 시간을 자동으로저장
    @Column(updatable = false)
    private LocalDateTime registeredTime;

    @LastModifiedDate//엔티티의 값을 변경할댜 시간을 자동으로 저장
    private LocalDateTime updateTime;
}
