package com.example.kpi_microservice.businessLayer.boundary;

import com.example.kpi_microservice.businessLayer.adapter.KpiDataSourceValue;
import com.example.kpi_microservice.domain.PatientInformation;
import com.example.kpi_microservice.domain.SurgeryInformation;
import com.example.kpi_microservice.domainLayer.Kpi;

import java.util.ArrayList;
import java.util.List;

public interface DataSourceGateway {

    ArrayList<KpiDataSourceValue> getSurgeryKpiValue(Kpi kpi, int daysRange);

    ArrayList<KpiDataSourceValue> getOperatingRoomsKpiValue(Kpi kpi, int daysRange, String orName);

    List<String> getRegisteredSurgeryTypes();

    List<String> getOperatingRoomNames();

    List<KpiDataSourceValue> getListKpi(int daysRange, String kpi);

    List<KpiDataSourceValue> getListKpiFilteredByType(int daysRange, String kpi, String type);

    PatientInformation getPatientInfo(String identifier);

    int waitingListConsistency();

    int overThresholdSurgeries();

    int meanWaitingTimeForPriority(String priority);

    SurgeryInformation getSurgeryInfo(String identifier);

    List<String> getOperationRoomDepartment(String orName);
}
