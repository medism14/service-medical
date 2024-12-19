package com.practitioner_service.practitioner_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8002");
        devServer.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setName("Support Médical");
        contact.setEmail("support@medical.com");
        contact.setUrl("https://www.medical-support.com");

        License mitLicense = new License()
            .name("MIT License")
            .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
            .title("API de Gestion des Praticiens")
            .version("1.0")
            .contact(contact)
            .description("Cette API permet de gérer les praticiens médicaux, incluant leurs informations personnelles et professionnelles.")
            .termsOfService("https://www.medical-support.com/terms")
            .license(mitLicense);

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer));
    }
} 