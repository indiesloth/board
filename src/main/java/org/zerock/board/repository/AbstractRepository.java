package org.zerock.board.repository;

import java.io.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

@NoRepositoryBean
public interface AbstractRepository<T, K extends Serializable> extends JpaRepository<T, K> {

}
