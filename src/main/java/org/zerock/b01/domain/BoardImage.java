package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Board board;


    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }


    // 연관관계 편의 메소드
    // 나중에 Board가 삭제되면 객체 참조를 변경하기 위함.
    public void changeBoard(Board board){
        this.board = board;
    }
}
