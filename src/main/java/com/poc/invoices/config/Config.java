package com.poc.invoices.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    
    
    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 getAazonS3(){
        return AmazonS3ClientBuilder.standard().withRegion(region).build();
    }
}
