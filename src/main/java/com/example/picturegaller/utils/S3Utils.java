package com.example.picturegaller.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class S3Utils {
    

    @Value("${s3.bucketName}")
    private String bucketName;


    private  AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1)
                .build();
    }


    public String uploadToS3(MultipartFile file, String key){
        /**
         * uploads image to s3 buckets
         * returns the presigned url for the s3
         */
        try {
        AmazonS3 s3 = this.s3Client();
        File uploadedFile = convertMultipartFileToFile(file);
        s3.putObject(this.bucketName, key, uploadedFile);
        String url = this.generatePresignedUrl(key, (long) 60*60*244);
        return  url;
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        
    }


    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Create a temporary file in the system's temporary directory
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());

        // Transfer the content of MultipartFile to the temporary file
        multipartFile.transferTo(file);

        return file;
    }

     public  String generateKey(String prefix) {
        // Get the current timestamp
        long timestamp = System.currentTimeMillis();

        // Generate a unique UUID
        String uuid = UUID.randomUUID().toString();

        // Combine prefix, timestamp, and UUID to create a unique key
        return prefix + "/" + timestamp + "-" + uuid;
    }


    private String generatePresignedUrl(String fileName, long expirationInMillis) {
        // Set the expiration time for the URL (e.g., 1 hour)
        Date expiration = new Date(System.currentTimeMillis() + expirationInMillis);

        // Generate the pre-signed URL request
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(this.bucketName, fileName)
                .withMethod(HttpMethod.GET) // HTTP method (GET to download the file)
                .withExpiration(expiration); // URL expiration time

        // Generate the pre-signed URL
        URL url = this.s3Client().generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString(); // Return the URL as a string
    }

       
       

     public Boolean deleteImageFromS3(String key) {
        try {
            // Create delete request with the object key
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
            
            // Delete the object from S3
            s3Client().deleteObject(deleteObjectRequest);
            
           return true;
        } catch (Exception e) {
            System.err.println("Error deleting image from S3: " + e.getMessage());
            return false;
        }
    }
}