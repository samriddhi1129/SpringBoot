package com.gla.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class.
 * @ComponentScan tells Spring: "look inside package com.gla and create objects (beans)
 * of every class which has @Component, @Service or @Repository".
 */
@Configuration
@ComponentScan(basePackages = "com.gla")
public class AppConfig {
}
