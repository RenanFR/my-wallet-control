package com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wallet.control.web.dto.StatementUploadDTO;
import com.wallet.control.web.model.BankStatement;
import com.wallet.control.web.service.BankStatementService;
import com.wallet.control.web.service.UploadService;
import com.wallet.control.web.service.readers.BankStatementReader;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("upload")
@Slf4j
public class UploadController {
	
	@Autowired
	private BankStatementService bankStatementService;
	
	@Autowired
	private ApplicationContext springContext;
	
	@Autowired
	private UploadService uploadService;	
	
	@PostMapping(consumes = {
			"multipart/form-data"
	})
	public ResponseEntity<String> upload(@ModelAttribute StatementUploadDTO uploadDTO) {
		try {
			String fileName = uploadDTO.getAccount().replace("-", "") + "_" + (uploadDTO.getPeriodStart().toString().replace("-", "")) + "-" + (uploadDTO.getPeriodEnd().toString().replace("-", "")) + "." + uploadDTO.getFileExtension().toString().toLowerCase();
			uploadService.uploadToBucket(fileName, uploadDTO.getFile());
			String qualifier = uploadDTO.getBank().toString().toLowerCase() + "-" + uploadDTO.getFileExtension().toString().toLowerCase() + "-reader";
			log.info("Looking for reader implementation with qualifier {} ", qualifier);
			BankStatementReader reader = springContext.getBean(qualifier, BankStatementReader.class);
			BankStatement bankStatement = reader.readAndParse(fileName, uploadDTO);
			bankStatement.setFileName(fileName);
			bankStatementService.save(bankStatement);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(uploadDTO.getAccount());
	}
	
}
