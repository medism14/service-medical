package com.appointment_service.appointment_service.config;

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
        devServer.setUrl("http://localhost:8003");
        devServer.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setName("Support Médical");
        contact.setEmail("support@medical.com");

        Info info = new Info()
                .title("API de Gestion des Rendez-vous")
                .version("1.0")
                .contact(contact)
                .description("Cette API permet de gérer les rendez-vous entre patients et praticiens.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
} 