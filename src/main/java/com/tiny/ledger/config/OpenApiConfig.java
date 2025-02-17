package com.tiny.ledger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Simple Tiny Ledger API")
            .version("1.0")
            .description("APIs to manage a tiny ledger system with deposits, withdrawals, current balance and transactions history.")
        );
  }
}