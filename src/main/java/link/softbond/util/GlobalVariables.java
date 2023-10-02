package link.softbond.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class GlobalVariables {

    @Value("${app.baseurl}")
    private String baseUrl;

    @Bean
    public String getBaseUrl() {
        return baseUrl;
    }

}
