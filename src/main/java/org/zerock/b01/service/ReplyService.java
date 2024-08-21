package org.zerock.b01.service;

import org.zerock.b01.dto.page.PageRequestDTO;
import org.zerock.b01.dto.page.PageResponseDTO;
import org.zerock.b01.dto.reply.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
