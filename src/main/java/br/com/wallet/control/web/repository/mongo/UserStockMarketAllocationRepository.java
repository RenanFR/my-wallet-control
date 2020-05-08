package br.com.wallet.control.web.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.wallet.control.web.model.UserStockMarketAllocation;

public interface UserStockMarketAllocationRepository extends MongoRepository<UserStockMarketAllocation, String> {

}
