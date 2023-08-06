package org.zerock.board.dto;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.*;

@Data
@AllArgsConstructor
public class UploadResultDTO {

  private String fileName;
  private String uuid;
  private String folderPath;

  public String getImageURL() {
    return URLEncoder.encode(folderPath + File.separator + uuid + "_" + fileName,
        StandardCharsets.UTF_8);
  }

  public String getThumbnailURL() {
    return URLEncoder.encode(folderPath + File.separator + "s_" + uuid + "_" + fileName,
        StandardCharsets.UTF_8);
  }
}
