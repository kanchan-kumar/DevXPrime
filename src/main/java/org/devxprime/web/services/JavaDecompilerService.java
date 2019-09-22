package org.devxprime.web.services;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.devxprime.web.exception.DecompilerErrorException;
import org.devxprime.web.props.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;

@Service
public class JavaDecompilerService implements DecompilerService {

    private final Path rootLocation;
    private FileReadWriteService fileReadWriteService;
    private static Logger logger = LogManager.getLogger(JavaDecompilerService.class);

    @Autowired
    public JavaDecompilerService(StorageProperties properties, FileReadWriteService fileReadWriteService) {
	this.rootLocation = Paths.get(properties.getLocation());
	this.fileReadWriteService = fileReadWriteService;
    }


    @Override
    public String decompile(String fileName, byte mode) throws Exception {
	return decompileProcyon(fileName);
    }

    @Override
    public String decompileProcyon(String fileName) throws Exception {

	String name = StringUtils.substring(fileName, 0, fileName.lastIndexOf('.'));
	String path = this.rootLocation.toFile().getAbsolutePath();
	String inputFile = path + "/" + name + ".class";
	String outputFile = path + "/" + name + ".java";
	File outputFileRef = new File(outputFile);
	logger.debug("inputFile = " + inputFile + ", outputFile = " + outputFile + ", name = " + name + ", path = " + path + ", fileName = " + fileName);

	if (outputFileRef.exists()) {
	    return fileReadWriteService.readFile(outputFileRef);
	} 

	final DecompilerSettings settings = DecompilerSettings.javaDefaults();

	try {

	    StringWriter stringWriter = new StringWriter();
	    PlainTextOutput output = new PlainTextOutput(stringWriter);

	    Decompiler.decompile(
		    inputFile,
		    output,
		    settings
		    );

	    String decompiledData = output.toString();
	    try {
		fileReadWriteService.writeToFile(outputFileRef, decompiledData);
	    } catch (Exception e) {

	    }
	    return decompiledData;		

	} catch (Exception e) {
	    throw new DecompilerErrorException("ERROR: Error in decompiling file.");
	}
    }
}
