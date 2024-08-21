package org.zerock.b01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *  기본적으로 JPA를 활용할때, Id를 PK로 지정해야 엔티티로 사용할 수 있다.
 *  @Column()으로 컬럼의 길이와 널 여부를 지정해준다.
 *  일반적으로 엔티티는 불변하게 설계해야 한다.
 *  수정해야 하는 경우 Setter 보다는 아래 change와 같은 방식을 활용하자.
 *
 *  후에 게시물 삭제처리 시, 댓글도 삭제해야 한다. ReplyRepository를 BoardService에 주입해서 처리한다.
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }

    // 지연 로딩, cascade = CascadeType.ALL은 Board가 삭제되면 연관된 이미지도 삭제된다. 그런데 첨부파일만 삭제하는 경우를 위해 orphanRemoval = true를 추가한다.
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .board(this)
                .uuid(uuid)
                .fileName(fileName)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){

        imageSet.forEach(boardImage -> {
            boardImage.changeBoard(null);
        });

        this.imageSet.clear();
    }
}
