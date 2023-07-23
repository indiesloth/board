package org.zerock.board.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
public class PageResultDTO<DTO, EN> {

  /*
    화면에서 필요한 결과에 대한 DTO
   */

  //DTO리스트
  private List<DTO> dtoList;

  //총 페이지 번호
  private int totalPage;

  //현재 페이지 번호
  private int page;
  //목록 사이즈
  private int size;

  //시작 페이지 번호, 끝 페이지 번호
  private int start, end;

  //이전, 다음
  private boolean prev, next;

  //페이지 번호 목록
  private List<Integer> pageList;

  /*
    화면에서 필요한 결과를 담는 DTO
    Function<T, R> : 객체 T를 객체 R로 변환
   */
  public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
    dtoList = result.stream().map(fn).collect(Collectors.toList());

    totalPage = result.getTotalPages();

    makePageList(result.getPageable());
  }

  private void makePageList(Pageable pageable) {
    /*
      처리된 결과에는 시작 페이지, 끝 페이지등 필요한 모든 정보를 담아서 화면에서는
      필요한 내용들만 찾아서 구성이 가능하도록 작성

      prev 1 2 3 4 [5] 6 7 8 9 10 next
      prev 11 12 13 14 15 16 17 18 [19] 20 next

      페이징 처리를 하기 위해 가장 필요한 정보는 현재 사용자가 보고 있는 페이지의 정보입니다.
      사용자가 5페이지를 본다면 화면의 페이지 번호는 1부터 시작하지만 사용자가 19페이지를
      본다면 11부터 시작해야 하기 때문 (화면에 10개씩 페이지 번호를 출력한다고 가정한 상태)

      흔히들 페이지를 계산할 때 시작 번호를 먼저 하려고 하지만, 오히려 끝 번호를 먼저 계산해
      두는 것이 수월합니다. 끝 번호는 다음과 같은 공식으로 구할 수 있습니다(페이지 번호 10개씩)

      Math.ceil()은 소수점을 올림으로 처리하기 때문에 다음과 같은 상황이 가능
      Math.Ceil(double a)
      * 1페이지의 경우 : Math.ceil(0.1) * 10 = 10
      * 10페이지의 경우 : Math.ceil(1) * 10 = 10
      * 11페이지의 경우 : Math.ceil(1.1) * 10 = 20

      * 파라미터가 더블이라 더블 처리를 위해 10.0으로 나눔
      tempEnd = (int)(Math.ceil(페이지 번호 / 10.0) * 10;

      끝 번호(endPage)는 아직 개선의 여지가 있습니다(때문에 변수명을 tempEnd로 설정합니다)
      그럼에도 끝 번호를 먼저 계산하는 이유는 시작 번호를 계산하기 수월하기 때문이다

      끝 번호는 실제 마지막 페이지와 다시 비교할 필요가 있습니다

     */
    this.page = pageable.getPageNumber() + 1; //0부터 시작하므로 1을 추가
    this.size = pageable.getPageSize();

    //temp end page
    int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

    start = tempEnd - 9;

    prev = start > 1;

    //end = Math.min(totalPage, tempEnd);
    end = totalPage > tempEnd ? tempEnd : totalPage;

    next = totalPage > tempEnd;

    pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
  }
}
