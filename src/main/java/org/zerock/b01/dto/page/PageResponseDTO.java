package org.zerock.b01.dto.page;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 화면에 DTO의 목록과 시작/끝 페이지에 대한 처리
 * @param <E>
 */

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

    //시작, 끝 페이지 번호
    private int start;
    private int end;

    // 이전,다음 페이지의 여부
    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){

        if(total <= 0){
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        // 화면에서의 마지막 번호에 대한 계산
        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;
        // 시작 번호
        this.start = this.end - 9;
        // 데이터의 개수를 계산한 마지막 번호
        int last = (int)(Math.ceil((total / (double)size)));

        this.end = end > last ? last : end;

        this.prev = this.start > 1;

        this.next = total > this.end * this.size;
    }
}
