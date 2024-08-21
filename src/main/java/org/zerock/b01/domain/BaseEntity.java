package org.zerock.b01.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @MappedSuperclass 로 공통 속성을 처리할 수 있다.
 * 데이터가 추가된 시간, 수정 시간 등의 요소를 주로 다룬다.
 * AuditingEntityListener를 적용해서 해당 엔티티가 추가되거나 변경되면 자동으로 값을 지정할 수 있고,
 * 이거를 사용하기 위해서는 프로젝트 설정(ex-어플리케아션클래스)에 @EnableJpaAuditing을 추가해줘야함.
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
