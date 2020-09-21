package com.poc.invoices.services;

import java.io.File;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AWSS3ServiceImpl implements AWSS3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

    private AmazonS3 amazonS3;

    @Autowired
    public AWSS3ServiceImpl(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    @Async
    public void upload(File file, String bucketName) {
        LOGGER.info("File upload in progress...");
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, file.getName(), file));
        } catch (AmazonServiceException e) {
            LOGGER.error("Amazon S3 couldn't upload", e);
            throw e;
        } catch (SdkClientException e) {
            LOGGER.error("Couldn't parse the response from Amazon S3", e);
            throw e;
        }

        LOGGER.info("File download in progress...");
    }


    @Override
    @Async
    public byte[] download(String fileName, String bucketName) {
        LOGGER.info("File download in progress...");
        byte[] content = null;
        try {
            S3Object s3Object = amazonS3.getObject(bucketName, fileName);
            content = IOUtils.toByteArray(s3Object.getObjectContent());
            s3Object.close();
        } catch (Exception e) {
            LOGGER.error("Couldn't download the file from Amazon S3", e);
        }
        return content;
    }

}
