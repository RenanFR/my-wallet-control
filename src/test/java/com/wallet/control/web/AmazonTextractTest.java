package com.wallet.control.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DocumentLocation;
import com.amazonaws.services.textract.model.FeatureType;
import com.amazonaws.services.textract.model.GetDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.GetDocumentAnalysisResult;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.services.textract.model.StartDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.StartDocumentAnalysisResult;
import com.wallet.control.web.model.BankStatementEntry;

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
			StartDocumentAnalysisRequest analyzeDocumentRequest = new StartDocumentAnalysisRequest()
					.withDocumentLocation(new DocumentLocation().withS3Object(s3Object))
					.withFeatureTypes(FeatureType.TABLES);
			StartDocumentAnalysisResult analyzeDocumentResult = textractClient.startDocumentAnalysis(analyzeDocumentRequest);
			String jobId = analyzeDocumentResult.getJobId();
			System.out.println("Document analysis task created with Job Id " + jobId);
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
					GetDocumentAnalysisRequest textDetectionRequest = new GetDocumentAnalysisRequest().withJobId(jobId);
					GetDocumentAnalysisResult textDetectionResult = textractClient.getDocumentAnalysis(textDetectionRequest);
					if (textDetectionResult.getJobStatus().equals("SUCCEEDED")) {
						System.out.println("Document analysis task " + jobId + " done");
						List<Block> blocks = textDetectionResult.getBlocks();
						System.out.println("Result " + textDetectionResult);
						assertNotNull(blocks);
						blocks.forEach(block -> {
							if (block.getBlockType().equals("TABLE")) {
								List<String> cellIds = block.getRelationships().get(0).getIds();
								List<String> allColumns = new ArrayList<>();
								for (String cellId : cellIds) {
									Block cell = blocks.stream().filter(b -> b.getId().equals(cellId)).findFirst().get();
									List<String> wordIds = cell.getRelationships().get(0).getIds();
									String column = "";
									for (String wordId : wordIds) {
										Block word = blocks.stream().filter(b -> b.getId().equals(wordId)).findFirst().get();
										column = column + " " + word.getText();
									}
									allColumns.add("_" + column);
								}
								List<List<String>> lines = ListUtils.partition(allColumns, 4);
								List<String> rows = new ArrayList<>();
								lines.forEach(l -> {
									String row = l.stream().collect(Collectors.joining());
									rows.add(row);
								});
								List<BankStatementEntry> entries = new ArrayList<>();
								rows.forEach(r -> {
									try {
										String[] toParse = r.split("_");
										Long page = Long.valueOf(block.getPage());
										LocalDate entryDate = LocalDate.parse(toParse[1].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
										String entryDescription = toParse[2].trim();
										BigDecimal entryValue = new BigDecimal(toParse[3].replace("R$", "").replace("  ", "").replace(".", "").replace(",", ".").trim());
										BigDecimal entryBalanceAfter = new BigDecimal(toParse[4].replace("R$", "").replace("  ", "").replace(".", "").replace(",", ".").trim());
										BankStatementEntry entry = BankStatementEntry.builder()
												.date(entryDate)
												.description(entryDescription)
												.value(entryValue)
												.balanceAfter(entryBalanceAfter)
												.lineNumber(page)
												.build();
										entries.add(entry);
										System.out.println(entry);										
									} catch (DateTimeParseException exception) {
										System.err.println("Error parsing line " + r);
										System.err.println(exception.getMessage());
									}
								});
								System.out.println(entries.size() + " lines parsed successfully");
							}
						});
						executorService.shutdown();
					} else {
						System.out.println("Document analysis task " + jobId + " in progress");
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
					
				}
			}
		}, 2, 5, TimeUnit.SECONDS);
		executorService.awaitTermination(60, TimeUnit.MINUTES);
	}
	
}
