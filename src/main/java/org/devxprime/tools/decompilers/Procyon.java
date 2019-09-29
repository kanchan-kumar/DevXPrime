package org.devxprime.tools.decompilers;

import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.DecompilerSettings;
import com.strobel.decompiler.PlainTextOutput;
import com.strobel.decompiler.languages.java.JavaFormattingOptions;
import com.strobel.decompiler.languages.java.JavaLanguage;

@Component
public class Procyon {

    private static Logger logger = LogManager.getLogger(Procyon.class);

    public String decompileFile(String inputFile) {
	try {

	    logger.info("Using Procyon decompiler to decompile file = " + inputFile);

	    final DecompilerSettings settings = DecompilerSettings.javaDefaults();
	    settings.setLanguage(new JavaLanguage());
	    settings.setForceExplicitImports(false);
	    settings.setIncludeLineNumbersInBytecode(true);
	    settings.setJavaFormattingOptions(JavaFormattingOptions.createDefault());
	    settings.setIncludeErrorDiagnostics(false);

	    StringWriter stringWriter = new StringWriter();
	    PlainTextOutput output = new PlainTextOutput(stringWriter);

	    Decompiler.decompile(
		    inputFile,
		    output,
		    settings
		    );

	    return output.toString();

	} catch (Exception e) {
	    logger.error("Exception on decompiling java file.", e);
	    return "Error in decompiling file.";
	}
    }

}
