package org.devxprime.web.services;

import java.io.File;

public interface FileReadWriteService {
	
	void writeToFile(File file, String data) throws Exception;

}
