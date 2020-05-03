package br.com.wallet.control.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.model.BankStatement;
import br.com.wallet.control.web.repository.mongo.BankStatementRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BankStatementService {
	
	@Autowired
	private BankStatementRepository repository;

	public BankStatement save(BankStatement statement) {
		log.info("Saving bank statement {} in the Mongo database ", statement);
		BankStatement saved = repository.save(statement);
		log.info("Bank statement saved with id {}", saved.get_id());
		return saved;
	}
	
	public List<BankStatement> findStatementByAccount(String account) {
		return repository
				.findByAccount(account);
	}
	
	public BankStatement findStatementByUploadId(String uploadId) {
		return repository
				.findById(uploadId)
				.get();
	}

}
