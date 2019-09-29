package org.devxprime.tools.decompilers;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JavaClass;

@Component
public class JadXDecompiler {
    private static Logger logger = LogManager.getLogger(JadXDecompiler.class);

    public String decompileFile(String inputFile, String uploadDirPath) {
	try {

	    logger.info("Using Jadx decompiler to decompile file = " + inputFile);

	    JadxArgs jadArgs = new JadxArgs();
	    jadArgs.setUseSourceNameAsClassAlias(true);
	    jadArgs.setRootDir(new File(uploadDirPath));
	    jadArgs.setInputFile(new File(inputFile));
	    JadxDecompiler decompiler = new JadxDecompiler(jadArgs);
	    decompiler.load();
	    List<JavaClass> arrClasses = decompiler.getClasses();

	    if (arrClasses != null && !arrClasses.isEmpty()) {
		return arrClasses.get(0).getCode();
	    } else {
		return null;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    logger.error("Exception on decompiling java file.", e);
	    return "Error in decompiling file.";
	}
    }
}
