package com.dmi.poc.services;

import io.minio.MinioClient;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log
public class MinioClientConfig {

    @Value("${app.minio.host}")
    private String minioHost;
    @Value("${app.minio.accessKey}")
    private String minioAccessKey;
    @Value("${app.minio.secretKey}")
    private String minioSecretKey;

    @Bean
    public MinioClient produceClient() {
        // Initialize minio client object.
        log.info("Initing MINIO Client");
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioHost)
                //.endpoint(minioHost, 9000, false)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
        minioClient.traceOn(System.out);
        return minioClient;
    }
}
