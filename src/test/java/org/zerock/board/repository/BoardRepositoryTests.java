package org.zerock.board.repository;


import java.util.*;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.zerock.board.entity.*;

@SpringBootTest
class BoardRepositoryTests {

  @Autowired
  private BoardRepository boardRepository;

  @Test
  public void insertBoard() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Member member = Member.builder().email("user" + i + "@aaa.com").build();
      Board board = Board.builder()
          .title("Title..." + i)
          .content("Content..." + i)
          .writer(member)
          .build();

      boardRepository.save(board);
    });
  }

  @Transactional
  @Test
  public void testRead1() {
    /*
      @ManyToOne(fetch = FetchType.LAZY) 속성으로 데이터베이스와 연결이 끝난 상태이기 때문에
      No Session 예외가 발생

      @Transactional 해당 메서드를 하나의 트랜잭션으로 처리하라는 의미
      트랜잭션으로 처리하면 속성에 따라 다르게 동작하지만, 기본적으로는 필요할 때 다시
      데이터베이스와 연결이 생성 됨
     */

    Optional<Board> result = boardRepository.findById(100L); //데이터베이스에 존재하는 번호

    Board board = result.get();

    System.out.println(board);
    System.out.println(board.getWriter());

  }

  @Test
  public void testReadWithWriter() {
    Object result = boardRepository.getBoardWithWriter(100L);

    Object[] arr = (Object[]) result;

    System.out.println("---------------------------------------");
    System.out.println(Arrays.toString(arr));
  }

  @Test
  public void testGetBoardWithReply() {
    List<Object[]> result = boardRepository.getBoardWithReply(100L);

    for (Object[] arr : result) {
      System.out.println(Arrays.toString(arr));
    }
  }

  @Test
  public void testWithReplyCount() {

    Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

    Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

    result.get().forEach(row -> {
      Object[] arr = row;
      System.out.println(Arrays.toString(arr));
    });
  }

  @Test
  public void testRead3() {

    Object result = boardRepository.getBoardByBno(100L);

    Object[] arr = (Object[]) result;

    System.out.println(Arrays.toString(arr));

  }

  @Test
  public void testSearch1() {
    boardRepository.search1();
  }

  @Test
  public void testSearch2() {
    boardRepository.search2();
  }

  @Test
  public void testSearch3() {
    boardRepository.search3();
  }

  @Test
  public void testSearchPage() {

    Pageable pagealbe = PageRequest.of(0, 10,
        Sort.by("bno").descending().and(Sort.by("title").ascending()));

    Page<Object[]> result = boardRepository.searchPage("t", "1", pagealbe);

  }
}