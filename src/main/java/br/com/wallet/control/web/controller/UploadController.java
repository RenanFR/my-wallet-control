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
import br.com.wallet.control.web.service.FileService;
import br.com.wallet.control.web.service.WebSocketService;

@Controller
@RequestMapping("upload")
public class UploadController {
	
	@Autowired
	private BankStatementService bankStatementService;
	
	@Autowired
	private FileService uploadService;	
	
	@Autowired
	private CloudMessagingService messageService;
	
	@Autowired
	private WebSocketService webSocketService;	
	
	@PostMapping(consumes = {
			"multipart/form-data" 
	})
	public ResponseEntity<String> upload(@ModelAttribute StatementUploadDTO uploadDTO) {
		String userId = DadosLogin.get().get_id();
		String fileName = userId.replace("-", "") + "_" + uploadDTO.getBankAccount() + "_" + (uploadDTO.getPeriodStart().toString().replace("-", "")) + "-" + (uploadDTO.getPeriodEnd().toString().replace("-", "")) + "." + uploadDTO.getFileExtension().toString().toLowerCase();
		BankStatement bankStatement = BankStatementAssembler.fromDTO(fileName, uploadDTO);
		try {
			bankStatement.setFileName(fileName);
			bankStatement.setUserId(userId);
			bankStatement.setStatus(JobStatus.IN_PROGRESS);
			String uploadId = bankStatementService.save(bankStatement).get_id();
			uploadService.upload(fileName, uploadDTO.getFile());
			messageService.publish(uploadId);
		} catch (Exception e) {
			e.printStackTrace();
			bankStatement.setStatus(JobStatus.FAILED);
			bankStatementService.save(bankStatement);
			webSocketService.notifyBankStatementStatus(bankStatement);
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(userId);
	}
	
}
