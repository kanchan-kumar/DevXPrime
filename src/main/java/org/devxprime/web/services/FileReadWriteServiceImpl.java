package org.devxprime.web.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.devxprime.web.exception.FileAlreadyExistException;
import org.devxprime.web.exception.FileWriteException;
import org.springframework.stereotype.Service;

@Service
public class FileReadWriteServiceImpl implements FileReadWriteService{

	@Override
	public void writeToFile(File file, String data) throws Exception {

		if (file.exists()) {
			throw new FileAlreadyExistException("File " + file.getName() + " already exist.");
		}
		
		try {
					
			try (FileWriter fileWriter = new FileWriter(file)) {
				fileWriter.write(data);
			}
			
		} catch (Exception e) {
			throw new FileWriteException("ERROR: " + e.getMessage() + " while writing in file.");
		}
	}

	@Override
	public String readFile(File file) throws Exception {
		
		try {
					
			return FileUtils.readFileToString(file, Charset.defaultCharset());
			
		} catch (Exception e) {
			throw new FileWriteException("ERROR: " + e.getMessage() + " while reading in file.");
		}	    
	}

}
