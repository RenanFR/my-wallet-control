package br.com.wallet.control.web.dto.binders;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import br.com.wallet.control.web.model.FileExtension;

@Component
public class FileExtensionConverter implements Converter<String, FileExtension> {

	@Override
	public FileExtension convert(String source) {
		return FileExtension.valueOf(source);
	}

}
