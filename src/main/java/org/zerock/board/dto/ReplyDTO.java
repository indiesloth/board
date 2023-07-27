package org.zerock.board.dto;

import java.time.LocalDateTime;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDTO {
  private Long rno;
  private String text;
  private String replyer;
  private Long bno;
  private LocalDateTime regDate, modDate;
}

