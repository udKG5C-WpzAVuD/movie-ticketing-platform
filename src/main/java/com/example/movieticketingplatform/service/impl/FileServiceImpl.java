package com.example.movieticketingplatform.service.impl;

import com.example.movieticketingplatform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${file-upload-path}") // 示例: ./ （开发环境）或 /var/www/（生产环境）
    private String baseDir;

    @Override
    public Map<String, String> upload(MultipartFile file) throws IOException {
       // 修正路径拼接方式
        String storageDir = Paths.get(baseDir,  "image").toString() + File.separator;
        return storeFile(file, storageDir);
    }

    private Map<String, String> storeFile(MultipartFile file, String storageDir) throws IOException {
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String uniqueFilename = UUID.randomUUID() + fileExtension; // 如 886f3fc0.jpg

        // 确保目录存在
        File dir = new File(storageDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        String filePath = storageDir + uniqueFilename;
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            out.write(file.getBytes());
        }

        // 返回统一访问路径（不含物理路径前缀）
        Map<String, String> result = new HashMap<>();
        result.put("url", "/image/" + uniqueFilename);
        log.info("文件存储路径：{}，访问URL：{}", filePath, result.get("url"));
        return result;
    }
}
