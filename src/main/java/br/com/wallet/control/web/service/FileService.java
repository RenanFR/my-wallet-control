package br.com.wallet.control.web.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	void upload(String fileName, MultipartFile file) throws Exception;
	
	String getFileURL(String fileKey);
	
	InputStream download(String fileKey);
	
	void remove(String fileName);

}
