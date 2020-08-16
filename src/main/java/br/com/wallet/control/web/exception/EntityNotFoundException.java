package br.com.wallet.control.web.exception;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "NÃO FOI POSSÍVEL ENCONTRAR {} NA BASE COM OS PARÂMETROS {}";
	
	public EntityNotFoundException(EntityType entityType, Object... params) {
		super((MESSAGE.substring(0, MESSAGE.indexOf("{")) + 
				entityType.getDomainDescription() + 
				MESSAGE.substring(MESSAGE.indexOf("}") + 1, MESSAGE.lastIndexOf("{")) + 
				Stream.of(Arrays.asList(params)).flatMap(p -> p.stream()).map(Object::toString).collect(Collectors.joining(", "))).toUpperCase());
		log.error(MESSAGE, entityType.getDomainDescription(), params);
	}
	
}
