package org.zerock.board.repository.search;

import com.querydsl.core.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.*;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements
    SearchBoardRepository {

  public SearchBoardRepositoryImpl() {
    super(Board.class);
  }

  @Override
  public Board search1() {
    /*
      Querydsl 라이브러리 내에는 JPQLQuery라는 인터페이스를 활용
     */

    log.info("search1..........");

    QBoard board = QBoard.board;

    JPQLQuery<Board> jpqlQuery = from(board);

    jpqlQuery.select(board).where(board.bno.eq(1L));
    //jpqlQuery.where(board.bno.eq(1L)).select(board); 반대로해도 같은 결과

    /*
      select board
      from Board board
      where board.bno = ?1
     */

    log.info("-----------------------------------------");
    log.info(jpqlQuery);
    log.info("-----------------------------------------");

    List<Board> result = jpqlQuery.fetch();

    return null;
  }

  @Override
  public Board search2() {

    log.info("search2..........");

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;

    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    /*
      select board
      from Board board
      left join Reply reply with reply.board = board
     */

    log.info("-----------------------------------------");
    log.info(jpqlQuery);
    log.info("-----------------------------------------");

    List<Board> result = jpqlQuery.fetch();

    return null;
  }

  @Override
  public Board search3() {

    log.info("search3..........");

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
    tuple.groupBy(board);

    log.info("-----------------------------------------");
    log.info(tuple);
    log.info("-----------------------------------------");

    List<Tuple> result = tuple.fetch();

    log.info(result);

    return null;
  }

  @Override
  public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

    log.info("searchPage....................");

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;
    QMember member = QMember.member;

    JPQLQuery<Board> jpqlQuery = from(board);
    jpqlQuery.leftJoin(member).on(board.writer.eq(member));
    jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

    //SELECT b, w, count(r) FROM Board b
    //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
    JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

    BooleanBuilder booleanBuilder = new BooleanBuilder();
    BooleanExpression expression = board.bno.gt(0L);

    booleanBuilder.and(expression);

    if (type != null) {
      String[] typeArr = type.split("");
      //검색 조건을 저장하기
      BooleanBuilder conditionBuilder = new BooleanBuilder();

      for (String t : typeArr) {
        switch (t) {
          case "t":
            conditionBuilder.or(board.title.contains(keyword));
            break;
          case "w":
            conditionBuilder.or(member.email.contains(keyword));
            break;
          case "c":
            conditionBuilder.or(board.content.contains(keyword));
            break;
        }
      }
      booleanBuilder.and(conditionBuilder);
    }

    tuple.where(booleanBuilder);

    tuple.groupBy(board);

    List<Tuple> result = tuple.fetch();

    log.info(result);

    return null;
  }
}
