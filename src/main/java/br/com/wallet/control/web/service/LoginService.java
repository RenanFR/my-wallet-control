package br.com.wallet.control.web.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.exception.EntityNotFoundException;
import br.com.wallet.control.web.exception.EntityType;
import br.com.wallet.control.web.exception.UserAlreadyExistsException;
import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.model.NewLoginDTO;
import br.com.wallet.control.web.repository.mongo.LoginRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService implements UserDetailsService {
	
	@Autowired
	private LoginRepository loginRepository;	
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return loginRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(EntityType.USER, email));
	}
	
	public Login registerUserAccount(NewLoginDTO newLoginDTO) {
		Login user = Login
			.builder()
			.userName(newLoginDTO.getUserName())
			.email(newLoginDTO.getUserEmail())
			.password(passwordEncoder.encode(newLoginDTO.getPassword()))
			.bankAccounts(newLoginDTO.getBankAccounts())
			.bitcoinBalance(newLoginDTO.getBitcoinBalance())
			.cpf(newLoginDTO.getCpf())
			.easynvestPassword(Base64.encodeBase64String(newLoginDTO.getEasynvestPassword().getBytes()))
			.build();
		loginRepository
			.findByEmail(user.getEmail())
			.ifPresent((u) -> {
				throw new UserAlreadyExistsException();
			});
		Login loginDocument = loginRepository.save(user);
		log.info("NEW DOCUMENT CREATED AT DATABASE FOR USER {} WITH ID {}", loginDocument.getUsername(), loginDocument.get_id());
		return loginDocument;
	}

}
