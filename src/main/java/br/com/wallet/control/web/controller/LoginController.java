package br.com.wallet.control.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.wallet.control.web.model.Login;
import br.com.wallet.control.web.model.NewLoginDTO;
import br.com.wallet.control.web.service.LoginService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("auth")
@Slf4j
public class LoginController {
	
	@Autowired
	private LoginService service;
	
	@PostMapping
	public ResponseEntity<Void> createNewUser(
			@RequestBody NewLoginDTO newLoginDTO
			) {
		log.info("CREATING NEW USER {}", newLoginDTO.toString());
		Login newUserAccount = service.registerUserAccount(newLoginDTO);
		log.info("NEW USER CREATED WITH IDENTIFIER {}", newUserAccount.get_id());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<Login> getUserInfo() {
		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(login);
		
	}

}
