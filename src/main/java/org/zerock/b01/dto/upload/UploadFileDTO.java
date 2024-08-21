package org.zerock.b01.dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 스웨거로 테스트하기위해 DTO를 추가하는 것이 좋다.
 *
 */

@Data
public class UploadFileDTO {
    private List<MultipartFile> files;
}
