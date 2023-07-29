package org.zerock.board.service;

import java.util.List;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.*;

public interface ReplyService {

  Long register(ReplyDTO replyDTO);

  List<ReplyDTO> getList(Long bno);

  void modify(ReplyDTO replyDTO);

  void remove(Long rno);

  Integer findByBnoWithCount(Long bno);

  default Reply dtoToEntity(ReplyDTO replyDTO) {
    Board board = Board.builder().bno(replyDTO.getBno()).build();
    return Reply.builder()
        .rno(replyDTO.getRno())
        .text(replyDTO.getText())
        .replyer(replyDTO.getReplyer())
        .board(board)
        .build();
  }

  default ReplyDTO entityToDTO(Reply reply) {
    return ReplyDTO.builder()
        .rno(reply.getRno())
        .text(reply.getText())
        .replyer(reply.getReplyer())
        .regDate(reply.getRegDate())
        .modDate(reply.getModDate())
        .build();
  }
}
