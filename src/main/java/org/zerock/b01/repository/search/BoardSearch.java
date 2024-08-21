package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.board.BoardListAllDTO;
import org.zerock.b01.dto.board.BoardListReplyCountDTO;

/**
 *  목록을 처리할 때는 반드시 limit와 같은 페이징 처리가 실행되는지 체크해라.
 *  성능에 중요하기 때문임.
 */

public interface BoardSearch {

    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types,
                                                      String keyword,
                                                      Pageable pageable);

    //Page<BoardListReplyCountDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types,
                                         String keyword,
                                         Pageable pageable);
}
