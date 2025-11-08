package com.example.kpi_microservice.businessLayer.boundary;

import com.example.kpi_microservice.businessLayer.adapter.KpiDataSourceValue;
import com.example.kpi_microservice.domainLayer.Kpi;

import java.util.ArrayList;
import java.util.List;

public interface DataSourceGateway {
    List<KpiDataSourceValue> getKpiValue(Kpi kpi);

    ArrayList<KpiDataSourceValue> getOperatingRoomKpi(Kpi kpi, String operatingRoomId);
}
