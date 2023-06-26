package link.softbond;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class UtilConfig {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public MessageSource messageSource() {
	  ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	  messageSource.setBasename("classpath:i18n/messages");
	  messageSource.setDefaultEncoding("UTF-8");
	  return messageSource;
	}
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

		
	@Bean
    public Logger logger() {
        return LoggerFactory.getLogger("MiLogger");
    }
	

}
