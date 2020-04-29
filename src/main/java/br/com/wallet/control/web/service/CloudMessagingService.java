package br.com.wallet.control.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import br.com.wallet.control.web.config.Properties;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CloudMessagingService {
	
	@Autowired
	private Properties properties;
	
	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	
	public void publish(String uploadId) {
		log.info("Publishing upload {} to process entries", uploadId);
		String queueName = properties.getQueueName();
		queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(uploadId).build());
		log.info("Bank statement file process message published in queue {}", queueName);
	}

}
