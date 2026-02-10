package com.example.kpi_microservice.domain;

import java.time.LocalDateTime;

public record SurgeryDate(LocalDateTime insertionDate, LocalDateTime executionDate) {
}
