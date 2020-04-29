package br.com.wallet.control.web.service.readers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DocumentLocation;
import com.amazonaws.services.textract.model.FeatureType;
import com.amazonaws.services.textract.model.GetDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.GetDocumentAnalysisResult;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.services.textract.model.StartDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.StartDocumentAnalysisResult;
import com.opencsv.exceptions.CsvException;

import br.com.wallet.control.web.config.Properties;
import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.BankStatementEntry;
import lombok.extern.slf4j.Slf4j;

@Service("inter-pdf-reader")
@Slf4j
public class InterBankStatementPDFReader implements BankStatementPDFReader {
	
	@Autowired
	private AmazonTextract textractClient;
	
	@Autowired
	private Properties properties;	

	@Override
	public List<BankStatementEntry> readAndParse(BankStatement bankStatement) throws IOException, CsvException, InterruptedException {
		//TODO Create aspect to log readers
		log.info("Starting bank statement entries processing for bank {} and extension {}", bankStatement.getBank().getName(), bankStatement.getFileExtension());		
		List<BankStatementEntry> entries = new ArrayList<>();
		S3Object s3Object = new S3Object()
				.withName(bankStatement.getFileName())
				.withBucket(properties.getBucketName());
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> task = executorService.schedule(() -> {
			StartDocumentAnalysisRequest analyzeDocumentRequest = new StartDocumentAnalysisRequest()
					.withDocumentLocation(new DocumentLocation().withS3Object(s3Object))
					.withFeatureTypes(FeatureType.TABLES);
			StartDocumentAnalysisResult analyzeDocumentResult = textractClient.startDocumentAnalysis(analyzeDocumentRequest);
			String jobId = analyzeDocumentResult.getJobId();
			log.info("Document analysis task created with Job Id {}", jobId);
			return jobId;
		}, 1, TimeUnit.SECONDS);
		executorService.scheduleWithFixedDelay(() -> {
			if (!task.isDone()) {
				log.info("Job creation in progress");
			}
			else {
				String jobId;
				try {
					jobId = (String) task.get();
					GetDocumentAnalysisRequest textDetectionRequest = new GetDocumentAnalysisRequest().withJobId(jobId);
					GetDocumentAnalysisResult textDetectionResult = textractClient.getDocumentAnalysis(textDetectionRequest);
					if (textDetectionResult.getJobStatus().equals("SUCCEEDED")) {
						log.info("Document analysis task {} done", jobId);
						List<Block> blocks = textDetectionResult.getBlocks();
						log.info("Result {}", textDetectionResult);
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
									} catch (DateTimeParseException exception) {
										log.error("Error parsing line {}", r);
										log.error(exception.getMessage());
									}
								});
								bankStatement.setEntries(entries);
								log.info("{} lines parsed successfully", entries.size());
							}
						});
						executorService.shutdown();
					} else {
						log.info("Document analysis task {} in progress", jobId);
					}
				} catch (Exception e) {
					log.error(e.getMessage());
					
				}
			}
		}, 2, 5, TimeUnit.SECONDS);
		executorService.awaitTermination(60, TimeUnit.MINUTES);
		return entries;
	}

}
