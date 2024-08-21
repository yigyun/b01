package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;

/**
 *  ToString은 참조하는 객체를 사용하지 않게 exclude 해줘야 한다.
 *  LAZY 속성 지정하기.
 *  쿼리 조건으로 자주 사용되는 칼럼에는 인덱스 생성하기
 *  @Table(name = "Reply", indexes = {
 *         @Index(name = "idx_reply_board_bno", columnList = "board_bno")
 * })
 */

@Entity
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;

    private String replyer;

    // 댓글 수정시에 text만 수정할 수 있게 하기 위해.
    public void changeText(String text){
        this.replyText = text;
    }
}
