package br.com.wallet.control.web.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.wallet.control.web.model.Login;

public interface LoginRepository extends MongoRepository<Login, String> {
	
	Optional<Login> findByEmail(String email);

}
