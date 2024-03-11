package com.dmi.poc.services;

import com.dmi.poc.dto.MinioItem;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    final
    AuditServices auditServices;


    public UploadService(MinioClient minioClient, AuditServices auditServices) {
        this.minioClient = minioClient;
        this.auditServices = auditServices;
    }

    public MinioItem upload(String bucket, MultipartFile file, String path, Map<String, String> userMetadata) {
        if(path!=null){
            path= path.trim();
        }
        if(StringUtils.isNotEmpty(path) && !path.endsWith("/")) {
            //throw new RuntimeException("Cannot deal with wrong path. Must end with /");
            path = path+"/";
        }
        MinioItem minioItem;
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
            minioItem = new MinioItem();
            minioItem.setSize((long) uploadResp.headers().size());
            minioItem.setName(uploadResp.object());
            GetPresignedObjectUrlArgs urlBuilder = GetPresignedObjectUrlArgs.builder()
                    .expiry(60 * 60 * 24) //TODO Move to config
                    .method(Method.GET)
                    .bucket(uploadResp.bucket())
                    .object(uploadResp.object())
                    .build();
            minioItem.setUrl(minioClient.getPresignedObjectUrl(urlBuilder));
            minioItem.setVersion(uploadResp.versionId());
            this.auditServices.logMessage("upload", "Uploaded object "+uploadResp.object()+" in bucket "+uploadResp.bucket());
        }catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return minioItem;
    }

    private String resolveObjectName(MultipartFile file, String path) {
       /* if(StringUtils.isNotEmpty(path)) {
            return StringUtils.isEmpty(file.getOriginalFilename())?path+file.getOriginalFilename():path+file.getName();
        }*/
        //TODO more filename cleanup.
        return file.getOriginalFilename();//StringUtils.isEmpty(file.getOriginalFilename())?file.getOriginalFilename():file.getName();
    }
}
