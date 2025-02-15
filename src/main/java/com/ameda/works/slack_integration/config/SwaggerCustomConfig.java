package com.ameda.works.slack_integration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCustomConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Slack Integration")
                        .description("Slack API Integration.")
                        .version("1.0").contact(new Contact().name("Developer Kevin Ameda Kisevu")
                                .email( "amedakevin@gmail.com").url("https://github.com/kisevu"))
                        .license(new License().name("License of API")
                                .url("API license URL")));
    }
}
