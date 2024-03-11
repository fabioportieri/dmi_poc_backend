package com.dmi.poc;

import com.dmi.poc.dto.*;
import com.dmi.poc.entities.AuditLogs;
import com.dmi.poc.services.AuditServices;
import com.dmi.poc.services.SearchService;
import com.dmi.poc.services.UploadService;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Log
public class ApiResource {

    final
    SearchService searchService;

    final
    UploadService uploadService;

    final
    AuditServices auditServices;

    public ApiResource(SearchService searchService, UploadService uploadService, AuditServices auditServices) {
        this.searchService = searchService;
        this.uploadService = uploadService;
        log.info("INited api resource....");
        this.auditServices = auditServices;
    }

    @GetMapping("/search")
    public PagedResponse<MinioItem> search(@RequestParam("q")String query,
                                           @RequestParam("bucket")String bucket,
                                @RequestParam(value = "sortAttr", required = false)String sortAttr,
                                @RequestParam(value = "sortDir", required = false)String sortDir,
                                @RequestParam(value = "limit", required = false) Integer limit,
                                @RequestParam(value = "start", required = false) Integer start,
                                @RequestParam(value = "tags", required = false)String tags) {

        if(StringUtils.isEmpty(bucket)) {
            throw new RuntimeException("Cannot proceed with bucket name");
        }
        if(limit==null || limit > 500) {
            limit = 100;
        }
        if(start==null) {
            start=0;
        }
        if(StringUtils.isEmpty(sortAttr)) {
            sortAttr = "name";
        }
        if(StringUtils.isEmpty(sortDir)) {
            sortDir="ASC";
        }

        SearchParams searchParams = new SearchParams();
        searchParams.setBucket(bucket);
        searchParams.setQuery(query);
        searchParams.setTags(tags);
        searchParams.setSortDir(sortDir);
        searchParams.setSortAttr(sortAttr);
        searchParams.setLimit(limit);
        searchParams.setStart(start);

        return this.searchService.searchMinioServer(searchParams);

    }

    @PostMapping("/upload")
    public MinioItem uploadObject(@RequestParam("file") MultipartFile file,
                                  @RequestParam("bucket")String bucket,
                                  @RequestParam(value = "path", required = false)String path) {
        return this.uploadService.upload(bucket, file, path, null);
    }

    @GetMapping("/buckets")
    public PagedResponse<MinioBucket> listBuckets() {
        return this.searchService.listBuckets();
    }

    @GetMapping("/logs")
    public PagedResponse<AuditLogs> searchLogs(
            @RequestParam(value = "operation", required = false)String operation,
            @RequestParam(value = "message", required = false)String message
    ) {
        return this.auditServices.searchLogs(operation, message);
    }

    @PostMapping("/logs")
    public void logsMessage(@RequestBody() LogMessageRequest logMessageRequest) {
        this.auditServices.logMessage(logMessageRequest.getOperation(), logMessageRequest.getMessage());
    }
}
