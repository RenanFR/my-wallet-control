package br.com.wallet.control.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.dto.BankStatementProcessingStatus;
import br.com.wallet.control.web.model.BankStatement;

@Service
public class WebSocketService {
	
	private static final String WEB_SOCKET_TOPIC = "/topic/statements/user/";
	
	@Autowired
	private SimpMessagingTemplate webSocketTemplate;
	
	public void notifyBankStatementStatus(BankStatement bankStatement) {
		webSocketTemplate.convertAndSend((WEB_SOCKET_TOPIC + bankStatement.getUserId()), new BankStatementProcessingStatus(bankStatement.get_id(), bankStatement.getStatus()));
	}

}
