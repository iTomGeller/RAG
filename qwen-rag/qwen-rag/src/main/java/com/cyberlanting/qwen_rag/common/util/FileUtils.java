package com.cyberlanting.qwen_rag.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FileUtils {

//    public static String parseFileToText(MultipartFile multipartFile) throws IOException {
//        File tempFile = File.createTempFile("upload-", ".tmp");
//        try {
//            multipartFile.transferTo(tempFile);
//            Tika tika = new Tika();
//            return tika.parseToString(tempFile);
//        } catch (TikaException e) {
//            log.info("Tika解析文件失败");
//            throw new RuntimeException(e);
//        } finally {
//            if (tempFile.exists()) {
//                tempFile.delete(); // 主动删除
//            }
//        }
//    }

    public static String parseFileToText(MultipartFile multipartFile) throws IOException {
        // 使用文件流直接读取，不创建临时文件
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Tika tika = new Tika();
            return tika.parseToString(inputStream);
        } catch (TikaException e) {
            log.error("Tika解析文件失败", e);
            throw new RuntimeException(e);
        }
    }

}
