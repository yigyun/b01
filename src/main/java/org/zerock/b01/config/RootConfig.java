package org.zerock.b01.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper 설정을 통해 DTO와 엔티티 간의 변환 처리를 간단하게 한다.
 * MSA에서는
 */


@Configuration
public class RootConfig {


    // 메소드에 대해서는 정보가 없길래 따로 작성

    /**
     * setFieldMatchingEnabled를 true로 하여 필드끼리 매칭을 가능하게 한다. 특정 필드도 제외 가능.
     * setFieldAccessLevel는 접근 권한에 따라 어떤 메서드, 필드가 매핑대상이 될지 결정한다. 여기서는 private 필드 매핑
     * setMatchingStrategy는 속성이 어떻게 매칭될지 결정한다. strict는 source와 destination의 프로퍼티가 완전히 같아야 매핑된다.
     * 보통 strict가 권장된다.
     */
    @Bean
    public ModelMapper getMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }

}
