package br.com.wallet.control.web.dto;

import br.com.wallet.control.web.model.BankStatement;

public class BankStatementAssembler {
	
	public static BankStatement fromDTO(String fileName, StatementUploadDTO uploadDTO) {
		return BankStatement.builder()
				.userId(uploadDTO.getUserId())
				.periodStart(uploadDTO.getPeriodStart())
				.periodEnd(uploadDTO.getPeriodEnd())
				.uploadedAt(uploadDTO.getUploadedAt())
				.fileName(fileName)
				.fileExtension(uploadDTO.getFileExtension())
				.bank(uploadDTO.getBank())
				.columnDate(uploadDTO.getColumnDate())
				.columnDescription(uploadDTO.getColumnDescription())
				.columnValue(uploadDTO.getColumnValue())
				.columnBalance(uploadDTO.getColumnBalance())
				.build();
	}

}
