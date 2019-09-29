package org.devxprime.web.services;

public interface DecompilerService {
	
	String decompile(String fileName, byte mode) throws Exception;
}
