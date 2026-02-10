package com.example.kpi_microservice.domain;

public record SurgeryInformation(String reason, String category, String codeCategory, String priorityKey, String executionStart, String waitingListInsertionDate, String isDone, String status, String surgeryCode) {
}
