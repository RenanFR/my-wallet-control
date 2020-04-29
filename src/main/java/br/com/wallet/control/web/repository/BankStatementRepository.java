package br.com.wallet.control.web.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.wallet.control.web.model.BankStatement;

public interface BankStatementRepository extends MongoRepository<BankStatement, String> {
	
	List<BankStatement> findByAccount(String account);

}
