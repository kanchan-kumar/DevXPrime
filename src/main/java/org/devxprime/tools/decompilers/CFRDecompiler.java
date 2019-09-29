package org.devxprime.tools.decompilers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.springframework.stereotype.Component;

@Component
public class CFRDecompiler {

    private static Logger logger = LogManager.getLogger(CFRDecompiler.class);

    public String decompileFile(String inputFile) {
	try {

	    logger.info("Using CFR decompiler to decompile file = " + inputFile);

	    Map<String, String> options = new HashMap<>();
	    options.put("comments", "false");
	    options.put("showversion", "false");
	    StringBuffer sb = new StringBuffer();
	    OutputSink<String> output = new OutputSink<>(sb);
	    OutputSinkFactory mySink = new OutputSinkFactory() {
		@Override
		public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
		    return Collections.singletonList(SinkClass.STRING);
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
		    return sinkType == SinkType.JAVA ? (Sink<T>) output : ignore -> {
		    };
		}
	    };

	    CfrDriver driver = new CfrDriver.Builder().withOutputSink(mySink).withOptions(options).build();
	    driver.analyse(Collections.singletonList(inputFile));
	    return sb.toString();

	} catch (Exception e) {
	    logger.error("Exception on decompiling java file.", e);
	    return "Error in decompiling file.";
	}
    }

}

class OutputSink<T> implements OutputSinkFactory.Sink<String> {

    StringBuffer buffer = null;

    OutputSink(StringBuffer buffer) {
	this.buffer = buffer;
    }

    @Override
    public void write(String sinkable) {
	if (buffer != null) {
	    buffer.append(sinkable);
	}
    }
}
