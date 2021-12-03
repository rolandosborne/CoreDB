package org.coredb;

import java.io.*;

import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.*;
import org.springframework.boot.builder.*;
import org.springframework.boot.web.support.*;
import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.core.io.*;
import org.springframework.core.io.support.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.data.jpa.repository.config.*;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling

@EnableJpaRepositories(basePackages = { "org.coredb.jpa.repository" })
@ComponentScan(basePackages = { "org.coredb" })
@EntityScan(basePackages = { "org.coredb" })

public class Application extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private static final String SERVER_CONFIG_PROPERTIES_PATH = "/data/config/config.properties";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
    Resource[] resources;
        PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
   if (new File(SERVER_CONFIG_PROPERTIES_PATH).exists()) {
      resources = new Resource[] {
        (new FileSystemResourceLoader()).getResource("file:" + SERVER_CONFIG_PROPERTIES_PATH),
      };
    } else {
      resources = new Resource[] {
        (new PathMatchingResourcePatternResolver()).getResource("classpath:config.properties"),
      };
    }
    propertyConfigurer.setLocations(resources);
		return propertyConfigurer;
	}
}

