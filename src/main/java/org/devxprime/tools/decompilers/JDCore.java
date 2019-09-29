package org.devxprime.tools.decompilers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.core.v1.api.printer.Printer;
import org.springframework.stereotype.Component;

@Component
public class JDCore {

    private static Logger logger = LogManager.getLogger(JDCore.class);

    private Loader loader = null;
    private Printer printer = null;

    public JDCore() {
    }

    public String decompileFile(String inputFile, String fileName) {
	try {

	    logger.info("Using JDCore decompiler to decompile file = " + inputFile + ", fileName = " + fileName);

	    createLoaderType();
	    getPrinter();
	    ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();
	    decompiler.decompile(loader, printer, inputFile);
	    return printer.toString();

	} catch (Exception e) {
	    logger.error("Exception on decompiling java file.", e);
	    return "Error in decompiling file.";
	}
    }

    private void createLoaderType() {

	loader = new Loader() {
	    @Override
	    public byte[] load(String internalName) throws LoaderException {
		InputStream is = null;
		try {

		    if (internalName.endsWith(".class")) {
			is = new FileInputStream(new File(internalName));
		    } else {
			is = this.getClass().getResourceAsStream("/" + internalName + ".class");
		    }
		} catch (IOException e) {
		    logger.error("Error on reading class.", e);
		}

		if (is == null) {
		    return null;
		} else {
		    try (InputStream in = is; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int read = in.read(buffer);

			while (read > 0) {
			    out.write(buffer, 0, read);
			    read = in.read(buffer);
			}

			return out.toByteArray();
		    } catch (IOException e) {
			throw new LoaderException(e);
		    }
		}
	    }

	    @Override
	    public boolean canLoad(String internalName) {
		return this.getClass().getResource("/" + internalName + ".class") != null;
	    }
	};
    }

    private void getPrinter() {

	try {

	    printer = new Printer() {
		protected static final String TAB = "  ";
		protected static final String NEWLINE = "\n";

		protected int indentationCount = 0;
		protected StringBuilder sb = new StringBuilder();
		private LinkedList<String> data = new LinkedList<>();
		private String packageName = "";

		@Override
		public String toString() {
		    this.processDecompiledData();
		    return sb.toString();
		}

		@Override
		public void start(int maxLineNumber, int majorVersion, int minorVersion) {
		}

		@Override
		public void end() {
		}

		@Override
		public void printText(String text) {		    
		    data.add(text);
		}

		@Override
		public void printNumericConstant(String constant) {
		    data.add(constant);
		}

		@Override
		public void printStringConstant(String constant, String ownerInternalName) {
		    data.add(constant);
		}

		@Override
		public void printKeyword(String keyword) {
		    data.add(keyword);
		}

		@Override
		public void printDeclaration(int type, String internalTypeName, String name, String descriptor) {

		    data.add(name);
		}

		@Override
		public void printReference(int type, String internalTypeName, String name, String descriptor,
			String ownerInternalName) {

		   /* if (type == 1) {
			try {
			    if (internalTypeName != null) {
				packageName = internalTypeName.substring(0, internalTypeName.lastIndexOf('/'));
				packageName.replace('/', '.');
			    }
			} catch (Exception e) {

			}
		    }*/

		    data.add(name);
		}

		@Override
		public void indent() {
		    this.indentationCount++;
		}

		@Override
		public void unindent() {
		    this.indentationCount--;
		}

		@Override
		public void startLine(int lineNumber) {
		    for (int i = 0; i < indentationCount; i++)
			data.add(TAB);
		}

		@Override
		public void endLine() {
		    data.add(NEWLINE);
		}

		@Override
		public void extraLine(int count) {
		    while (count-- > 0)
			data.add(NEWLINE);
		}

		@Override
		public void startMarker(int type) {
		}

		@Override
		public void endMarker(int type) {
		}

		private void processDecompiledData() {
		    try {

			if (packageName == null) {
			    return;
			}

			int k = 0;
			for (String token: data) {
			    if (token.equals("package")) {
				//sb.append(token);
				//sb.append(" ");
				//sb.append(packageName);
				k = 3;
			    } else {
				
				if (k > 0) {
				    k--;
				    continue;
				}
				
				sb.append(token);
			    }

			}

		    } catch (Exception e) {

		    }
		}
	    };

	} catch (Exception e) {
	    logger.error("Error on getting printer object.", e);
	}

    }
}
