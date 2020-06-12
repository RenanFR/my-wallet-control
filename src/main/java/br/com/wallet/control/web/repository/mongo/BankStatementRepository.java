package br.com.wallet.control.web.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.wallet.control.web.model.BankStatement;

public interface BankStatementRepository extends MongoRepository<BankStatement, String> {
	
	List<BankStatement> findByUserId(String userId);

}
