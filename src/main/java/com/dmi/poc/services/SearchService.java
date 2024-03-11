package com.dmi.poc.services;

import com.dmi.poc.dto.MinioBucket;
import com.dmi.poc.dto.MinioItem;
import com.dmi.poc.dto.PagedResponse;
import com.dmi.poc.dto.SearchParams;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class SearchService {


    final
    MinioClient minioClient;

    final
    AuditServices auditServices;

    public SearchService(MinioClient minioClient, AuditServices auditServices) {
        this.minioClient = minioClient;
        this.auditServices = auditServices;
    }

    public PagedResponse<MinioItem> searchMinioServer(SearchParams searchParams) {
        PagedResponse<MinioItem> searchResult = new PagedResponse<MinioItem>();
        searchResult.setContent(new ArrayList<>());
        try {
            ListObjectsArgs.Builder listObjectArgsBuilder = ListObjectsArgs
                    .builder()
                    .bucket(searchParams.getBucket())
                    .prefix(searchParams.getQuery())
                    .recursive(true)
                    .maxKeys(searchParams.getLimit());
            ListObjectsArgs args = listObjectArgsBuilder.build();

            Iterable<Result<Item>> objects = minioClient.listObjects(args);
            for (Result<Item> result : objects) {
                Item item = result.get();
                //System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());

                // Create a new Album Object
                MinioItem minioItem = new MinioItem();
                minioItem.setSize(item.size());
                minioItem.setName(item.objectName());
                minioItem.setMetadata(item.userMetadata());
                minioItem.setLastModified(item.lastModified());
                minioItem.setStorageClass(item.storageClass());
                minioItem.setVersion(item.versionId());
                minioItem.setIsDir(item.isDir());
                if(item.owner()!=null) {
                    minioItem.setOwnerDisplayName(item.owner().displayName());
                }
                // Set the presigned URL in the album object
                GetPresignedObjectUrlArgs urlBuilder = GetPresignedObjectUrlArgs.builder()
                        .expiry(60 * 60 * 24) //TODO Move to config
                        .method(Method.GET)
                        .bucket(searchParams.getBucket())
                        .object(item.objectName())
                        .build();
                minioItem.setUrl(minioClient.getPresignedObjectUrl(urlBuilder));

                // Add the album object to the list holding Album objects
                searchResult.getContent().add(minioItem);

            }
            this.auditServices.logMessage("search", "Query: "+searchParams+", result count: "+searchResult.getContent().size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return searchResult;
    }

    public PagedResponse<MinioItem> listBucketObjects() {
        return null;
    }

    public PagedResponse<MinioBucket> listBuckets() {
        PagedResponse<MinioBucket> buckets = new PagedResponse<>();
        buckets.setContent(new ArrayList<>());
        try {
            List<Bucket> bucketList = this.minioClient.listBuckets();
            log.info("Bucket listing: "+bucketList);
            for (Bucket bucket:bucketList) {
                MinioBucket b = new MinioBucket();
                b.setName(bucket.name());
                b.setCreationDate(bucket.creationDate());
                buckets.getContent().add(b);
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return buckets;
    }
}
