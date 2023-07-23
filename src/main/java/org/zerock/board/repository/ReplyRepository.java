package org.zerock.board.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.*;

public interface ReplyRepository extends AbstractRepository<Reply, Long> {

  @Modifying
  @Query("delete from Reply r where r.board.bno = :bno")
  void deleteByBno(Long bno);
}
