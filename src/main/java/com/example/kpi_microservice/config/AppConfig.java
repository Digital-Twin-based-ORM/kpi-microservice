package com.example.kpi_microservice.config;

import com.example.kpi_microservice.businessLayer.UseCase;
import com.example.kpi_microservice.businessLayer.boundary.InputBoundary;
import com.example.kpi_microservice.persistence.KpiRepository;
import com.example.kpi_microservice.persistence.RemoteSparqlRepository;
import com.example.kpi_microservice.utils.EnvironmentVariable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public InputBoundary input() {
        String remoteHost = "http://" + EnvironmentVariable.WODT_HOST_ENV.getValue() + ":" + EnvironmentVariable.WODT_PORT_ENV.getValue();
        System.out.println("Remote host: " + remoteHost);
        RemoteSparqlRepository remoteRepository = new RemoteSparqlRepository(remoteHost);
        KpiRepository kpiRepository = new KpiRepository(remoteRepository);
        return new UseCase(kpiRepository);
    }

}
