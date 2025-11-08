package com.example.kpi_microservice.businessLayer.adapter;

import java.time.LocalDateTime;

public record KpiDataSourceValue(int id, float value, LocalDateTime timestamp) {
}
