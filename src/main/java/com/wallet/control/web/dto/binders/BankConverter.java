package com.wallet.control.web.dto.binders;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.wallet.control.web.model.Bank;

@Component
public class BankConverter  implements Converter<String, Bank> {

	@Override
	public Bank convert(String source) {
		return source.equals(Bank.INTER.getName())? Bank.INTER : Bank.ITAU;
	}

}
