package br.com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.dto.BankStatementAssembler;
import br.com.wallet.control.web.dto.StatementUploadDTO;
import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.model.DadosLogin;
import br.com.wallet.control.web.model.JobStatus;
import br.com.wallet.control.web.service.BankStatementService;
import br.com.wallet.control.web.service.CloudMessagingService;
import br.com.wallet.control.web.service.UploadService;

@Controller
@RequestMapping("upload")
public class UploadController {
	
	@Autowired
	private BankStatementService bankStatementService;
	
	@Autowired
	private UploadService uploadService;	
	
	@Autowired
	private CloudMessagingService messageService;
	
	@PostMapping(consumes = {
			"multipart/form-data" 
	})
	public ResponseEntity<String> upload(@ModelAttribute StatementUploadDTO uploadDTO) {
		String userId = DadosLogin.get().get_id();
		try {
			String fileName = userId.replace("-", "") + "_" + uploadDTO.getBankAccount() + "_" + (uploadDTO.getPeriodStart().toString().replace("-", "")) + "-" + (uploadDTO.getPeriodEnd().toString().replace("-", "")) + "." + uploadDTO.getFileExtension().toString().toLowerCase();
			BankStatement bankStatement = BankStatementAssembler.fromDTO(fileName, uploadDTO);
			bankStatement.setFileName(fileName);
			bankStatement.setUserId(userId);
			bankStatement.setStatus(JobStatus.IN_PROGRESS);
			String uploadId = bankStatementService.save(bankStatement).get_id();
			uploadService.uploadToBucket(fileName, uploadDTO.getFile());
			messageService.publish(uploadId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(userId);
	}
	
}
