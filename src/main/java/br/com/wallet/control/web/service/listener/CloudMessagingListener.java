package br.com.wallet.control.web.service.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import br.com.wallet.control.web.config.Properties;
import br.com.wallet.control.web.dto.BankStatementProcessingStatus;
import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.BankStatementEntry;
import br.com.wallet.control.web.model.JobStatus;
import br.com.wallet.control.web.service.BankStatementService;
import br.com.wallet.control.web.service.readers.BankStatementReader;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("sqs")
@Slf4j
public class CloudMessagingListener {
	
	@Autowired
	private ApplicationContext springContext;
	
	@Autowired
	private BankStatementService bankStatementService;
	
	@Autowired
	private Properties properties;
	
	@Autowired
	private AmazonS3 s3;	
	
	@Autowired
	private SimpMessagingTemplate webSocketTemplate;	
	
	private static final String WEB_SOCKET_TOPIC = "/topic/statements/user/";
	
	@SqsListener("test-messaging")
    public void receiveAndProcessUploadEntries(String message, @Header("SenderId") String senderId) 
    		throws IOException {
    	log.info("RECEIVED NEW MESSAGE WITH TEXT {} AND SENDER {}", message, senderId);
    	BankStatement bankStatement = bankStatementService.findStatementByUploadId(message);
    	S3Object fileS3 = s3.getObject(properties.getBucketName(), bankStatement.getFileName());
    	InputStream inputStreamS3 = fileS3.getObjectContent();
		bankStatement.setFile(new MockMultipartFile(fileS3.getKey(), inputStreamS3));
		String qualifier = bankStatement.getBank().toString().toLowerCase() + "-" + bankStatement.getFileExtension().toString().toLowerCase() + "-reader";
		log.info("LOOKING FOR READER IMPLEMENTATION WITH QUALIFIER {}", qualifier);
		BankStatementReader reader = springContext.getBean(qualifier, BankStatementReader.class);
		List<BankStatementEntry> entries;
		try {
			entries = reader.readAndParse(bankStatement);
			bankStatement.setEntries(entries);
			bankStatement.setStatus(JobStatus.SUCCEEDED);
		} catch (Exception exception) {
			log.error("ERROR WHILE READING THE STATEMENT FILE AND PARSING THE ENTRIES: {}", exception.getMessage());
			bankStatement.setStatus(JobStatus.FAILED);
		}
		bankStatementService.save(bankStatement);   
		webSocketTemplate.convertAndSend((WEB_SOCKET_TOPIC + bankStatement.getUserId()), new BankStatementProcessingStatus(bankStatement.get_id(), bankStatement.getStatus()));
    }	

}
