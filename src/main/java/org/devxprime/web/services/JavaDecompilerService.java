package org.devxprime.web.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.devxprime.tools.decompilers.CFRDecompiler;
import org.devxprime.tools.decompilers.JDCore;
import org.devxprime.tools.decompilers.JadXDecompiler;
import org.devxprime.tools.decompilers.Procyon;
import org.devxprime.utils.DecompilerConst;
import org.devxprime.web.exception.DecompilerErrorException;
import org.devxprime.web.props.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
public class JavaDecompilerService implements DecompilerService {

    private final Path rootLocation;
    private FileReadWriteService fileReadWriteService;
    private Procyon procyon;
    private JDCore jdCore;
    private CFRDecompiler cfr;
    private JadXDecompiler jadx;

    private static Logger logger = LogManager.getLogger(JavaDecompilerService.class);

    @Autowired
    public JavaDecompilerService(StorageProperties properties, FileReadWriteService fileReadWriteService,
	    Procyon procyon, JDCore jdCore, CFRDecompiler cfr, JadXDecompiler jadx) {
	this.rootLocation = Paths.get(properties.getLocation());
	this.fileReadWriteService = fileReadWriteService;

	this.procyon = procyon;
	this.jdCore = jdCore;
	this.cfr = cfr;
	this.jadx = jadx;
    }

    @Override
    public String decompile(String fileName, byte mode) throws Exception {
	return decompileFileByType(fileName, mode);
    }

    
    public String decompileFileByType(String fileName, int mode) throws Exception {

	String name = StringUtils.substring(fileName, 0, fileName.lastIndexOf('.'));
	String path = this.rootLocation.toFile().getAbsolutePath();
	String inputFile = path + "/" + name + ".class";
	String outputFile = path + "/" + name + ".java";
	File outputFileRef = new File(outputFile);
	logger.debug("inputFile = " + inputFile + ", outputFile = " + outputFile + ", name = " + name + ", path = "
		+ path + ", fileName = " + fileName);

	/*if (outputFileRef.exists()) {
	    return fileReadWriteService.readFile(outputFileRef);
	}*/

	try {

	    String decompiledData = null;

	    switch (mode) {

	    case DecompilerConst.PROCYON:
		decompiledData = procyon.decompileFile(inputFile);
		break;

	    case DecompilerConst.CFR:
		decompiledData = cfr.decompileFile(inputFile);
		break;

	    case DecompilerConst.JADX:
		decompiledData = jadx.decompileFile(inputFile, path);
		break;

	    default:
		decompiledData = jdCore.decompileFile(inputFile, name);

	    }

	    fileReadWriteService.writeToFile(outputFileRef, decompiledData);
	    return decompiledData;

	} catch (Exception e) {
	    e.printStackTrace();
	    throw new DecompilerErrorException("ERROR: Error in decompiling file.");
	}
    }
    
    
    public static void main(String []args) {
	try {
	    	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
