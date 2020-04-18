package com.wallet.control.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wallet.control.web.model.BankStatement;

public interface BankStatementRepository extends MongoRepository<BankStatement, String> {

}
