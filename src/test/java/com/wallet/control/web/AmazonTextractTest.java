package com.wallet.control.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DocumentLocation;
import com.amazonaws.services.textract.model.GetDocumentTextDetectionRequest;
import com.amazonaws.services.textract.model.GetDocumentTextDetectionResult;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.services.textract.model.StartDocumentTextDetectionRequest;
import com.amazonaws.services.textract.model.StartDocumentTextDetectionResult;

@SpringBootTest
public class AmazonTextractTest {
	
	@Autowired
	private AmazonTextract textractClient;
	
	@Test
	public void shouldReadPDF() throws IOException, InterruptedException, ExecutionException {
		String s3FileKey = "Extrato de Conta Corrente.pdf";
		String analysisBucket = "wallet-control-textract";
		S3Object s3Object = new S3Object()
				.withName(s3FileKey)
				.withBucket(analysisBucket);
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> task = executorService.schedule(() -> {
			StartDocumentTextDetectionRequest analyzeDocumentRequest = new StartDocumentTextDetectionRequest()
					.withDocumentLocation(new DocumentLocation().withS3Object(s3Object));
			StartDocumentTextDetectionResult analyzeDocumentResult = textractClient.startDocumentTextDetection(analyzeDocumentRequest);
			String jobId = analyzeDocumentResult.getJobId();
			System.out.println("Text detection task created with Job Id " + jobId);
			return jobId;
		}, 1, TimeUnit.SECONDS);
		executorService.scheduleWithFixedDelay(() -> {
			if (!task.isDone()) {
				System.out.println("Job creation in progress");
			}
			else {
				String jobId;
				try {
					jobId = (String) task.get();
					GetDocumentTextDetectionRequest textDetectionRequest = new GetDocumentTextDetectionRequest().withJobId(jobId);
					GetDocumentTextDetectionResult textDetectionResult = textractClient.getDocumentTextDetection(textDetectionRequest);
					if (textDetectionResult.getJobStatus().equals("SUCCEEDED")) {
						System.out.println("Text detection task " + jobId + " done");
						List<Block> blocks = textDetectionResult.getBlocks();
						System.out.println("Result " + textDetectionResult);
						assertNotNull(blocks);
						blocks.forEach(block -> {
							System.out.println("Block Type " + block.getBlockType() + ", "
									+ "Id " + block.getId() + ", "
									+ "Page " + block.getPage() + ", "
									+ "Text " + block.getText() + "");
						});
						executorService.shutdown();
					} else {
						System.out.println("Text detection task " + jobId + " in progress");
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
					
				}
			}
		}, 2, 5, TimeUnit.SECONDS);
		executorService.awaitTermination(60, TimeUnit.MINUTES);
	}
	
}
