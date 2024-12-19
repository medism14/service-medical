package com.patient_service.patient_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8001");
        devServer.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setName("API Support");
        contact.setEmail("support@example.com");

        Info info = new Info()
                .title("API de Gestion des Patients")
                .version("1.0")
                .contact(contact)
                .description("Cette API fournit des endpoints pour gérer les patients.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
} 