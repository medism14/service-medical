package com.gateway_service.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Patient Service Routes
                .route("patient-service", r -> r
                        .path("/patients/**")
                        .uri("http://localhost:8001"))
                
                // Practitioner Service Routes
                .route("practitioner-service", r -> r
                        .path("/practitioners/**")
                        .uri("http://localhost:8002"))
                
                // Appointment Service Routes
                .route("appointment-service", r -> r
                        .path("/appointments/**")
                        .uri("http://localhost:8003"))
                
                // Medical Record Service Routes
                .route("medical-record-service", r -> r
                        .path("/medical-records/**")
                        .uri("http://localhost:8004"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
} 