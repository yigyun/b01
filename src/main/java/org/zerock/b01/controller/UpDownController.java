package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    /**
     * 파일 저장시 같은 이름의 파일이 문제가 될 수 있으니 UUID를 붙여서 저장한다.
     * transfer 메소드로 간단하게 파일 업로드를 완료한다.
     * 이때 이미지 파일이면 썸네일을 생성한다.
     * @param uploadFileDTO
     * @return
     */

    @ApiOperation(value = "Upload Post", notes = "Post 방식으로 파일 등록하기")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null){

                final List<UploadResultDTO> list = new ArrayList<>();

                uploadFileDTO.getFiles().forEach(multipartFile -> {

                    String originalName = multipartFile.getOriginalFilename();
                    log.info(originalName);

                    String uuid = UUID.randomUUID().toString();

                    Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

                    boolean image = false;


                    try{
                        multipartFile.transferTo(savePath);

                        if(Files.probeContentType(savePath).startsWith("image")){

                            image = true;
                            File thumbFile = new File(uploadPath, "s_"+uuid+"_"+originalName);
                            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                        }
                    } catch (IOException e ){
                        e.printStackTrace();
                    }

                    list.add(UploadResultDTO
                            .builder()
                                    .uuid(uuid)
                                    .fileName(originalName)
                                    .img(image)
                            .build());
            });

                return list;
        }

        return null;
    }


    @ApiOperation(value = "view 파일", notes = "Get으로 파일을 조회한다.")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        log.info("fileName: " + fileName);

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @ApiOperation(value = "remove 파일", notes = "DELETE 방식으로 파일을 삭제한다.")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                thumbnailFile.delete();
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }
}
