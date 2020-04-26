package com.wallet.control.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class Properties {
	
	@Value("${amazon.bucket}")
	private String bucketName;	

}
