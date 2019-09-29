package org.devxprime.main;

import org.devxprime.tools.decompilers.CFRDecompiler;
import org.devxprime.tools.decompilers.JDCore;
import org.devxprime.tools.decompilers.JadXDecompiler;
import org.devxprime.tools.decompilers.Procyon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public Procyon procyon() {
	return new Procyon();
    }

    @Bean
    public JDCore jdCore() {
	return new JDCore();
    }

    @Bean
    public CFRDecompiler cfr() {
	return new CFRDecompiler();
    }

    @Bean
    public JadXDecompiler jadx() {
	return new JadXDecompiler();
    }
}
