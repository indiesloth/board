package org.zerock.board.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

  private final ReplyService replyService;

  @GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {
    log.info("bno: " + bno);

    return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
  }
}
