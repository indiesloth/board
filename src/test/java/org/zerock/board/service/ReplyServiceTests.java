package org.zerock.board.service;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.ReplyDTO;

@SpringBootTest
class ReplyServiceTests {

  @Autowired
  private ReplyService service;

  @Test
  public void testGetList() {
    Long bno = 100L;

    List<ReplyDTO> replyDTOList = service.getList(bno);

    replyDTOList.forEach(System.out::println);
  }
}