package org.zerock.board.repository;

import java.util.*;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.*;

@SpringBootTest
class ReplyRepositoryTests {

  @Autowired
  private ReplyRepository replyRepository;

  @Test
  public void insertReply() {
    IntStream.rangeClosed(1, 300).forEach(i -> {
      //1부터 100까지의 임의의 번호
      Long bno = (long) (Math.random() * 100) + 1;

      Board board = Board.builder().bno(bno).build();

      Reply reply = Reply.builder()
          .text("Reply..." + i)
          .board(board)
          .replyer("guest")
          .build();

      replyRepository.save(reply);
    });
  }

  @Test
  public void readReply1() {
    Optional<Reply> result =  replyRepository.findById(1L);

    Reply reply = result.get();

    System.out.println(reply);
    System.out.println(reply.getBoard());
  }

  @Test
  public void testListByBoard() {
    List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());

    replyList.forEach(System.out::println);
  }

  @Test
  public void testCountReply() {
    System.out.println("ReplyCount : " + replyRepository.findByBnoWithCount(98L));
  }
}