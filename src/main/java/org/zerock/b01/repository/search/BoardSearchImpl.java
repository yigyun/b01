package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.board.BoardImageDTO;
import org.zerock.b01.dto.board.BoardListAllDTO;
import org.zerock.b01.dto.board.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용하는 방법 순서
 * Querydsl을 이용할 인터페이스 선언.
 * 인터페이스이름+Impl 으로 클래스 선언, Impl 클래스에는 QuerydslRepositorySupport라는 부모 클래스 지정
 * 기존 repository, 여기서는 BoardRepository 부분에 부모 인터페이스로 Querydsl을 위한 인터페이스 지정하기.
 */

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable){
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board); // select .. from board

        BooleanBuilder  booleanBuilder = new BooleanBuilder();

        booleanBuilder.or(board.title.contains("1")); // where title like ...

        booleanBuilder.or(board.content.contains("11")); // content like ...

        query.where(booleanBuilder); // where 절에 추가

        query.where(board.bno.gt(0L)); // bno > 0

        this.getQuerydsl().applyPagination(pageable, query); // paging

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }//end if

        //bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }

    /**
     * 단방향 참조가 가지는 단점은 필요한 정보를 접근하기 어려운건데
     * 이거를 join으로 해결한다.
     * 특정 게시글에는 댓글이 없을 수 있는데 이때는 outer join으로 처리한다.
     * JPA는 프로젝션을 통해 바로 JPQL 결과를 DTO로 받을 수 있게 할 수 있다.
     *
     */

    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } // end for
            query.where(booleanBuilder);
        }

        // bno > 0 를 where 절에 추가.
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections
                .bean(BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board)); // leftjoin

        // 검색 조건과 키워드를 반영한다.
        if( (types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            } // end for
            boardJPQLQuery.where(booleanBuilder);
        }

        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery); // paging

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {

            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getCreatedAt())
                    .replyCount(replyCount)
                    .build();

            // BoardImage를 BoardImageDTO 처리하는 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .ord(boardImage.getOrd())
                        .uuid(boardImage.getUuid())
                        .fileName(boardImage.getFileName())
                        .build()
            ).collect(Collectors.toList());

            dto.setBoardImages(imageDTOS); // 이미지 처리한 것을 추가한다.

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

  /*  @Override
    public Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));

        getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        List<Board> boardList = boardJPQLQuery.fetch();

        boardList.forEach(board1 -> {
            System.out.println(board1.getBno());
            System.out.println(board1.getImageSet());
            System.out.println("-------------------");
        });

        return null;
    }*/


}
