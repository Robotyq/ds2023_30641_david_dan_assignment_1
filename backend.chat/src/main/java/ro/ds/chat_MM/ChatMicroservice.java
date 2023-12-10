package ro.ds.chat_MM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated
public class ChatMicroservice extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ChatMicroservice.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ChatMicroservice.class);
    }

}
