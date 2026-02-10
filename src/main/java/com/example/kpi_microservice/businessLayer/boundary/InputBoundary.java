package com.example.kpi_microservice.businessLayer.boundary;

import com.example.kpi_microservice.businessLayer.adapter.KpiDataSourceValue;
import com.example.kpi_microservice.businessLayer.adapter.KpiMeanValue;
import com.example.kpi_microservice.businessLayer.adapter.KpiMeanValueTyped;
import com.example.kpi_microservice.domain.PatientInformation;
import com.example.kpi_microservice.domain.SurgeryInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface InputBoundary {
    List<String> getRegisteredSurgeryTypes();

    List<KpiDataSourceValue> getListKpi(int daysRange, String kpi);

    List<KpiDataSourceValue> getListKpiFilteredByType(int daysRange, String kpi, String type);

    Optional<KpiMeanValue> getChirurgicMeanTime(int daysRange);

    Optional<KpiMeanValue> getChirurgicMeanTimeFilteredByType(int daysRange, String type);

    Optional<KpiMeanValue> getAnestMeanTimeFilteredByType(int daysRange, String type);

    Optional<KpiMeanValue> getMeanTouchTimeFilteredByType(int daysRange, String type);

    Optional<KpiMeanValue> getValueAddedMeanTimeFilteredByType(int daysRange, String type);

    Optional<KpiMeanValue> getAnestMeanTime(int daysRange);

    Optional<KpiMeanValue> getMeanTouchTime(int daysRange);

    Optional<KpiMeanValue> getValueAddedMeanTime(int daysRange);

    Optional<KpiMeanValue> getStartTimeTardinessForSurgeries(int daysRange, String type);

    Optional<KpiMeanValue> getStartTimeTardiness(String orName, int daysRange);

    Optional<KpiMeanValue> getSurgeriesPercentage(String orName, int daysRange);

    Optional<List<KpiMeanValueTyped>> getMeanStartTimeTardinessForSurgeries(int daysRange);

    ArrayList<KpiDataSourceValue> getOperatingRoomsKpiValues(String kpi, int range, String orName);

    List<String> getOperatingRoomNames();

    int waitingListConsistency();

    int overThresholdSurgeries();

    PatientInformation getPatientInfo(String identifier);

    SurgeryInformation getSurgeryInfo(String identifier);

    int meanWaitingTimeForPriority(String priority);

    List<String> getOperationRoomDepartment(String orName);
}
