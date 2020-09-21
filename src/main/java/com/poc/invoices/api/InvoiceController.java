package com.poc.invoices.api;

import java.io.IOException;


import com.poc.invoices.services.AWSS3Service;
import com.poc.invoices.utils.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private AWSS3Service awsS3Service;

    @Autowired
    InvoiceController(AWSS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @PostMapping("/upload")
    public void upload(@RequestPart(value = "file") MultipartFile file) throws IOException {
        awsS3Service.upload(FileUtils.toFile(file), bucketName);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam(value = "fileName") String fileName)
            throws IOException {
        final byte[] data = awsS3Service.download(fileName, bucketName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok().contentLength(data.length)
        .header("Content-type", "application/octet-stream")
        .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
        .body(resource);
    }

}
