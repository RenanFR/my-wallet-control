package br.com.wallet.control.web.service;

import java.util.List;
import java.util.Optional;

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
		log.info("SAVING BANK STATEMENT {} IN THE MONGO DATABASE ", statement);
		BankStatement saved = repository.save(statement);
		log.info("BANK STATEMENT SAVED WITH ID {}", saved.get_id());
		return saved;
	}
	
	public List<BankStatement> findStatementByUserId(String userId) {
		return repository
				.findByUserId(userId);
	}
	
	public BankStatement findStatementByUploadId(String uploadId) {
		return repository
				.findById(uploadId)
				.get();
	}
	
	public Optional<BankStatement> findStatementByEntryId(String entryId) {
		return repository
				.findByEntriesHash(entryId);
	}

}
