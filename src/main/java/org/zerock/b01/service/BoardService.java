package org.zerock.b01.service;

import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.board.BoardDTO;
import org.zerock.b01.dto.board.BoardListAllDTO;
import org.zerock.b01.dto.board.BoardListReplyCountDTO;
import org.zerock.b01.dto.page.PageRequestDTO;
import org.zerock.b01.dto.page.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    // 게시글의 이미지와 댓글 숫자 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .content(boardDTO.getContent())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }

        return board;
    }

    default BoardDTO entityToDTO(Board board){

            BoardDTO boardDTO = BoardDTO.builder()
                    .bno(board.getBno())
                    .content(board.getContent())
                    .title(board.getTitle())
                    .writer(board.getWriter())
                    .regDate(board.getCreatedAt())
                    .modDate(board.getUpdatedAt())
                    .build();

            List<String> fileNames = board.getImageSet().stream().sorted().map(image ->
                image.getUuid()+"_"+image.getFileName()
            ).collect(Collectors.toList());

            boardDTO.setFileNames(fileNames);

            return boardDTO;
    }
}
