package org.zerock.board.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

public interface BoardRepository extends AbstractRepository<Board, Long>, SearchBoardRepository {

  //한개의 로우(Object) 내에 Object[ ]로 나옴
  @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
  Object getBoardWithWriter(@Param("bno") Long bno);

  @Query("select b, r from Board b left join Reply r on r.board = b where b.bno =:bno")
  List<Object[]> getBoardWithReply(@Param("bno") Long bno);

  @Query(value = "SELECT b, w, count(r) "
      + " FROM Board b "
      + " LEFT JOIN b.writer w "
      + " LEFT JOIN Reply r ON r.board = b "
      + " GROUP BY b", countQuery = "SELECT count(b) FROM Board b")
  Page<Object[]> getBoardWithReplyCount(Pageable pageable);

  @Query("SELECT b, w, count(r) "
      + " FROM Board b LEFT JOIN b.writer w "
      + " LEFT OUTER JOIN Reply r ON r.board = b "
      + " WHERE b.bno = :bno")
  Object getBoardByBno(@Param("bno") Long bno);

}
