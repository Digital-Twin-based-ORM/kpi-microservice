package com.example.kpi_microservice.businessLayer;

import com.example.kpi_microservice.businessLayer.boundary.InputBoundary;
import com.example.kpi_microservice.persistence.KpiRepository;

public class UseCase implements InputBoundary {
    private final KpiRepository kpiRepository;

    public UseCase(KpiRepository kpiRepository) {
        this.kpiRepository = kpiRepository;
    }
}
