package org.zerock.b01.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 페이징 관련 정보와 검색의 종류, 키워드를 지정한다.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type; // 검색 종류 t,c,w,tc,tw,twc

    private String keyword;

    // 검색 조건과 페이징 조건을 문자열로 구성
    private String link;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }

    // #참고사항 여기서 props 사용법은 대충 이해감. 근데 확실히 해둘것.
    public Pageable getPageable(String...props){
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    public String getLink(){

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0){
                builder.append("&type=" + type);
            }

            if(keyword != null){
                try{
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                }catch (UnsupportedEncodingException e){
                    // 후에 여기도 채우기
                }
            }
            link = builder().toString();
        }

        return link;
    }
}
