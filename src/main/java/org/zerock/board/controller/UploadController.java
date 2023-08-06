package org.zerock.board.controller;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.board.dto.UploadResultDTO;

@RestController
@Log4j2
public class UploadController {

  @Value("${org.zerock.upload.path}")
  private String uploadPath;

  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

    List<UploadResultDTO> resultDTOList = new ArrayList<>();

    for (MultipartFile uploadFile : uploadFiles) {

      //이미지 파일만 업로드 가능
      if (!uploadFile.getContentType().startsWith("image")) {
        log.warn("this file is not image type");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }

      /*
      예전에는 IE나 Edge에서 전체 경로가 들어왔을지 모르지만
      IE와 Edge환경에서 테스트 결과 파일 이름만 들어오므로
      아래의 작업은 필수가 아님
     */
      //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
      String originalName = uploadFile.getOriginalFilename();
      String fileName = Objects.requireNonNull(originalName)
          .substring(originalName.lastIndexOf("\\") + 1);

      //client 에서 보낸 파일의 이름
      log.info("1.fileName : " + fileName);

      //날짜 폴더 생성
      String folderPath = makeFolder();
      log.info("2. folderPath : " + folderPath);

      //UUID
      String uuid = UUID.randomUUID().toString();
      log.info("3. uuid : " + uuid);

      //저장할 파일 이름 중간에 "_"를 이용해서 구분
      String saveName =
          uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
      log.info("4. saveName : " + saveName);

      Path savePath = Paths.get(saveName);
      log.info("5. savePath : " + savePath);

      try {
        //원본 파일 저장
        uploadFile.transferTo(savePath);

        //썸네일 저징 이름 생성
        String thumbnailSaveName =
            uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_"
                + fileName;

        //썸네일 파일 이름은 중간에 s_로 시작하도록
        File thumbnailFile = new File(thumbnailSaveName);

        //썸네일 생성
        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

        resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
  }

  @GetMapping("/display")
  public ResponseEntity<byte[]> getFile(String fileName) {

    ResponseEntity<byte[]> result = null;

    try {
      String srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

      log.info("fileName : " + srcFileName);

      File file = new File(uploadPath + File.separator + srcFileName);

      log.info("file : " + file);

      HttpHeaders header = new HttpHeaders();

      /*
        MIME은 이메일과 함께 동봉할 파일을 텍스트 문자로 전환해서 이메일 시스템을 통해 전달하기 위해
        개발되었기 때문에 이름에 Internet Mail Extension가 포함됩니다.
        현재는 웹을 통해서 여러 형태의 파일을 전달하는 데 쓰이고 있습니다.
       */
      //MIME타입 처리 - 확장자에 따라서 브라우저에 전송하는 MIME 타입이 달라져야 하는 문제 처리
      header.add("Content-Type", Files.probeContentType(file.toPath()));

      //파일 데이터 처리
      result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  @PostMapping("/removeFile")
  public ResponseEntity<Boolean> removeFile(String fileName) {

    String srcFileName = null;
    try {
      srcFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
      File file = new File(uploadPath + File.separator + srcFileName);
      boolean result = file.delete();

      File thumbnail = new File(file.getParent(), "s_" + file.getName());
      result = thumbnail.delete();

      return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String makeFolder() {
    String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    log.info("오늘 날짜 : " + str);

    String folderPath = str.replace("//", File.separator);
    log.info("OS별 파일 형식으로 변경 : " + folderPath);

    // make folder ----------
    File uploadPathFolder = new File(uploadPath, folderPath);
    log.info("uploadPathFolder : " + uploadPathFolder);

    if (!uploadPathFolder.exists()) {
      uploadPathFolder.mkdirs();
    }
    return folderPath;
  }
}
