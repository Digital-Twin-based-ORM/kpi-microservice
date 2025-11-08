package com.example.kpi_microservice.config;

import com.example.kpi_microservice.businessLayer.UseCase;
import com.example.kpi_microservice.businessLayer.boundary.InputBoundary;
import com.example.kpi_microservice.persistence.KpiRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public InputBoundary input() {
        KpiRepository kpiRepository = new KpiRepository();
        return new UseCase(kpiRepository);
    }

}
