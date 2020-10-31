package br.com.wallet.control.web.service.cloud;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.Tag;

import br.com.wallet.control.web.config.Properties;
import br.com.wallet.control.web.service.FileService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AmazonS3FileService implements FileService {
	
	@Autowired
	private AmazonS3 s3;
	
	@Autowired
	private Properties properties;
	
	public void upload(String fileName, MultipartFile file) throws AmazonServiceException, SdkClientException, IOException {
        PutObjectRequest putRequest = new PutObjectRequest(properties.getBucketName(), fileName, file.getInputStream(), new ObjectMetadata());
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new Tag("original_filename", file.getOriginalFilename()));
        tags.add(new Tag("uploaded_at", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
        putRequest.setTagging(new ObjectTagging(tags));
		PutObjectResult putResult = s3.putObject(putRequest);
		log.info("Object {} uploaded to bucket {} with generated cache ETag {} and version {}", fileName, properties.getBucketName(), putResult.getETag(), putResult.getVersionId()); 
	}
	
	public String getFileURL(String fileKey) {
		log.info("Generating pre signed URL for file {}", fileKey);
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(properties.getBucketName(), fileKey)
        		.withMethod(HttpMethod.GET)
        		.withExpiration(expiration);
        URL url = s3.generatePresignedUrl(request);
        String preSignedUrl = url.toString();
        log.info("Successfully created pre signed URL {} with expiration at {}", preSignedUrl);
		return preSignedUrl;
		
	}
	
	public InputStream download(String fileKeyS3) {
		S3Object fileS3 = s3.getObject(properties.getBucketName(), fileKeyS3);
    	InputStream inputStreamS3 = fileS3.getObjectContent();
		return inputStreamS3;
	}

	@Override
	public void remove(String fileName) {
		s3.deleteObject(new DeleteObjectRequest(properties.getBucketName(), fileName));
	}

}
