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

  @PostMapping("")
  public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
    log.info(replyDTO);

    Long rno = replyService.register(replyDTO);

    return new ResponseEntity<>(rno, HttpStatus.OK);
  }

  @DeleteMapping("/{rno}")
  public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
    log.info("rno : " + rno);

    replyService.remove(rno);

    return new ResponseEntity<>("success", HttpStatus.OK);
  }

  @PutMapping("/{rno}")
  public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
    log.info(replyDTO);

    replyService.modify(replyDTO);

    return new ResponseEntity<>("success", HttpStatus.OK);
  }

  @GetMapping("/count/{bno}")
  public ResponseEntity<Integer> findByBnoWithCount(@PathVariable("bno") Long bno) {
    log.info("bno: " + bno);

    Integer count = replyService.findByBnoWithCount(bno);

    return new ResponseEntity<>(count, HttpStatus.OK);
  }
}
