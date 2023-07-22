package org.zerock.board.entity;


import javax.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity {

  @Id
  private String email;

  private String password;

  private String name;
}
