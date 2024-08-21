package org.zerock.b01.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 게시글과 댓글의 관계에서 가장 고민할 부분이 목록이다.
 * 기존 목록화면은 Board 객체를 BoardDTO로 변환하지만 댓글이 추가되면서,
 * 특정 게시물의 댓글 숫자를 같이 출력해줘야 한다.
 */

@Data
public class BoardListReplyCountDTO {

    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private Long replyCount;
}
