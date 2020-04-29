package br.com.wallet.control.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.wallet.control.web.model.BankStatementEntry;

public interface BankStatementEntryRepository extends MongoRepository<BankStatementEntry, String> {

}
