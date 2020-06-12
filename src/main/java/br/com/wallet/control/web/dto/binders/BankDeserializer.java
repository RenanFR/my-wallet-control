package br.com.wallet.control.web.dto.binders;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import br.com.wallet.control.web.model.Bank;

@Component
public class BankDeserializer extends JsonDeserializer<Bank> {

	@Override
	public Bank deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String bankName = jsonParser.getText();
		return Bank.parseFromName(bankName);
	}

}
