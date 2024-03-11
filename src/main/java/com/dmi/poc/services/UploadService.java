package com.dmi.poc.services;

import com.dmi.poc.dto.MinioItem;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Service
@Log
public class UploadService {

    final
    MinioClient minioClient;

    public UploadService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public MinioItem upload(String bucket, MultipartFile file, String path, Map<String, String> userMetadata) {
        if(path!=null){
            path= path.trim();
        }
        if(StringUtils.isNotEmpty(path) && !path.endsWith("/")) {
            //throw new RuntimeException("Cannot deal with wrong path. Must end with /");
            path = path+"/";
        }
        try {
            //first create the path if present.
          /*  if (StringUtils.isNotEmpty(path) && path.endsWith("/")) {
                ObjectWriteResponse pathResp = minioClient.putObject(
                        PutObjectArgs.builder().bucket(bucket).object(path).stream(
                                        new ByteArrayInputStream(new byte[]{}), 1, -1)
                                .build());
                log.info("Path create response: "+pathResp.object()+", in bucket: "+pathResp.bucket());
            }*/
            String objectName = this.resolveObjectName(file, path);
            InputStream bais = file.getInputStream();
            ObjectWriteResponse uploadResp = minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket)
                            .object(objectName).stream(
                                    bais, bais.available(), -1)
                            .build());
            log.info("file create response: "+uploadResp.object()+", in bucket: "+uploadResp.bucket());
        }catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    private String resolveObjectName(MultipartFile file, String path) {
       /* if(StringUtils.isNotEmpty(path)) {
            return StringUtils.isEmpty(file.getOriginalFilename())?path+file.getOriginalFilename():path+file.getName();
        }*/
        //TODO more filename cleanup.
        return file.getOriginalFilename();//StringUtils.isEmpty(file.getOriginalFilename())?file.getOriginalFilename():file.getName();
    }
}
