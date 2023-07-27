package org.zerock.board.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.zerock.board.entity.*;

public interface ReplyRepository extends AbstractRepository<Reply, Long> {

  @Modifying
  @Query("delete from Reply r where r.board.bno = :bno")
  void deleteByBno(Long bno);

  List<Reply> getRepliesByBoardOrderByRno(Board board);
}
