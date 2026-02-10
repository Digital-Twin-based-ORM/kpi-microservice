package com.example.kpi_microservice.businessLayer;

import com.example.kpi_microservice.businessLayer.adapter.KpiDataSourceValue;
import com.example.kpi_microservice.businessLayer.adapter.KpiMeanValue;
import com.example.kpi_microservice.businessLayer.adapter.KpiMeanValueTyped;
import com.example.kpi_microservice.businessLayer.boundary.InputBoundary;
import com.example.kpi_microservice.domain.PatientInformation;
import com.example.kpi_microservice.domain.SurgeryInformation;
import com.example.kpi_microservice.domainLayer.Kpi;
import com.example.kpi_microservice.persistence.KpiRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UseCase implements InputBoundary {
    private final KpiRepository kpiRepository;

    public UseCase(KpiRepository kpiRepository) {
        this.kpiRepository = kpiRepository;
    }

    @Override
    public List<String> getRegisteredSurgeryTypes() {
        return kpiRepository.getRegisteredSurgeryTypes();
    }

    @Override
    public List<KpiDataSourceValue> getListKpi(int daysRange, String kpi) {
        return kpiRepository.getListKpi(daysRange, kpi);
    }

    @Override
    public List<KpiDataSourceValue> getListKpiFilteredByType(int daysRange, String kpi, String type) {
        return kpiRepository.getListKpiFilteredByType(daysRange, kpi, type);
    }

    @Override
    public Optional<KpiMeanValue> getChirurgicMeanTime(int daysRange) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpi(daysRange, "M14");
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M14", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getChirurgicMeanTimeFilteredByType(int daysRange, String type) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpiFilteredByType(daysRange, "M14", type);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M14", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getAnestMeanTimeFilteredByType(int daysRange, String type) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpiFilteredByType(daysRange, "M15", type);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M15", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getMeanTouchTimeFilteredByType(int daysRange, String type) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpiFilteredByType(daysRange, "M17", type);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M17", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getValueAddedMeanTimeFilteredByType(int daysRange, String type) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpiFilteredByType(daysRange, "M26", type);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M26", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getAnestMeanTime(int daysRange) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpi(daysRange, "M15");
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M15", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getMeanTouchTime(int daysRange) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpi(daysRange, "M17");
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M17", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getValueAddedMeanTime(int daysRange) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpi(daysRange, "M26");
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M26", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getStartTimeTardinessForSurgeries(int daysRange, String type) {
        List<KpiDataSourceValue> list = kpiRepository.getListKpiFilteredByType(daysRange, "M10", type);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M10", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getStartTimeTardiness(String orName, int daysRange) {
        List<KpiDataSourceValue> list = kpiRepository.getOperatingRoomsKpiValue(Kpi.M10, daysRange, orName);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M10", meanValue(list)));
        }
    }

    @Override
    public Optional<KpiMeanValue> getSurgeriesPercentage(String orName, int daysRange) {
        List<KpiDataSourceValue> list = kpiRepository.getOperatingRoomsKpiValue(Kpi.M16, daysRange, orName);
        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new KpiMeanValue("M16", meanValue(list)));
        }
    }

    @Override
    public Optional<List<KpiMeanValueTyped>> getMeanStartTimeTardinessForSurgeries(int daysRange) {
        List<String> surgeryTypes = this.getRegisteredSurgeryTypes();
        List<KpiMeanValueTyped> list = new ArrayList<>();
        surgeryTypes.forEach(type -> {
            Optional<KpiMeanValue> meanValue = this.getStartTimeTardinessForSurgeries(daysRange, type);
            meanValue.ifPresent(kpiMeanValue -> list.add(new KpiMeanValueTyped(kpiMeanValue.kpi(), kpiMeanValue.meanValue(), type)));
        });

        if(list.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(list);
        }
    }

    @Override
    public ArrayList<KpiDataSourceValue> getOperatingRoomsKpiValues(String kpi, int range, String orName) {
        return this.kpiRepository.getOperatingRoomsKpiValue(Kpi.valueOf(kpi), range, orName);
    }

    @Override
    public List<String> getOperatingRoomNames() {
        return this.kpiRepository.getOperatingRoomNames();
    }

    @Override
    public int waitingListConsistency() {
        return this.kpiRepository.waitingListConsistency();
    }

    @Override
    public int overThresholdSurgeries() {
        return this.kpiRepository.overThresholdSurgeries();
    }

    @Override
    public PatientInformation getPatientInfo(String identifier) {
        return this.kpiRepository.getPatientInfo(identifier);
    }

    @Override
    public SurgeryInformation getSurgeryInfo(String identifier) {
        return this.kpiRepository.getSurgeryInfo(identifier);
    }

    @Override
    public int meanWaitingTimeForPriority(String priority) {
        return this.kpiRepository.meanWaitingTimeForPriority(priority);
    }

    @Override
    public List<String> getOperationRoomDepartment(String orName) {
        return this.kpiRepository.getOperationRoomDepartment(orName);
    }

    private float meanValue(List<KpiDataSourceValue> list) {
        float result = list.stream().map(KpiDataSourceValue::value).reduce(0.0f, Float::sum);
        return result /  list.size();
    }
}
