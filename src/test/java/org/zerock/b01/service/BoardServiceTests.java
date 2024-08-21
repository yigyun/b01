package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.board.BoardDTO;
import org.zerock.b01.dto.board.BoardImageDTO;
import org.zerock.b01.dto.board.BoardListAllDTO;
import org.zerock.b01.dto.page.PageRequestDTO;
import org.zerock.b01.dto.page.PageResponseDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno: "+bno);
    }

    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated...101")
                .content("Updated content 101...")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID().toString()+"_zzz.jpg"));

        boardService.modify(boardDTO);

    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages(){

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID().toString() + "_aaa.jpg",
                        UUID.randomUUID().toString() + "_bbb.jpg",
                        UUID.randomUUID().toString() + "_bbb.jpg"
                ));

        Long bno = boardService.register(boardDTO);

        log.info("bno: "+bno);
    }

    @Test
    public void testReadAll(){

        Long bno = 1L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }
    }

    @Test
    public void testRemoveAll(){

            Long bno = 1L;

            boardService.remove(bno);
    }

    @Test
    public void testListWithAll(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO pageResponseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = pageResponseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null){
                for(BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages()){
                    log.info(boardImageDTO);
                }
            }
            log.info("------------------------------------------------");
        });
    }
}
