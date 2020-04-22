package com.wallet.control.web.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Tag;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UploadService {
	
	@Value("${amazon.bucket}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3;
	
	public void uploadToBucket(String fileName, MultipartFile file) throws AmazonServiceException, SdkClientException, IOException {
        PutObjectRequest putRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata());
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("original_filename", file.getOriginalFilename()));
        tags.add(new Tag("uploaded_at", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        putRequest.setTagging(new ObjectTagging(tags));
		PutObjectResult putResult = s3.putObject(putRequest);
		log.info("Object {} uploaded to bucket {} with generated cache ETag {} and version {}", fileName, bucketName, putResult.getETag(), putResult.getVersionId()); 
	}

}
