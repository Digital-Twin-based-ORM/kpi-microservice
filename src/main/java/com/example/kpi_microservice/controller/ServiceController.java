package com.example.kpi_microservice.controller;

import com.example.kpi_microservice.businessLayer.adapter.*;
import com.example.kpi_microservice.businessLayer.boundary.InputBoundary;
import com.example.kpi_microservice.domain.PatientInformation;
import com.example.kpi_microservice.domain.SurgeryInformation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@RestController
public class ServiceController {

    private final InputBoundary useCase;

    public ServiceController(InputBoundary useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/getSurgeriesMeanTime/{range}")
    public HttpEntity<SurgeriesMeanTimes> getMeanTimes(@PathVariable int range) {
        Optional<KpiMeanValue> chirTime = this.useCase.getChirurgicMeanTime(range);
        Optional<KpiMeanValue> anestTime = this.useCase.getAnestMeanTime(range);
        Optional<KpiMeanValue> touchTime = this.useCase.getMeanTouchTime(range);
        Optional<KpiMeanValue> valueAddedTime = this.useCase.getValueAddedMeanTime(range);
        if(chirTime.isEmpty() || anestTime.isEmpty() || touchTime.isEmpty() || valueAddedTime.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new SurgeriesMeanTimes(chirTime.get(), anestTime.get(), touchTime.get(), valueAddedTime.get()), HttpStatus.ACCEPTED);
    }

    @PutMapping("/getSurgeriesMeanTime/{range}")
    public HttpEntity<SurgeriesMeanTimes> getMeanTimes(@PathVariable int range, @RequestBody SurgeryTypeRequest type) {
        Optional<KpiMeanValue> chirTime = this.useCase.getChirurgicMeanTimeFilteredByType(range, type.type());
        Optional<KpiMeanValue> anestTime = this.useCase.getAnestMeanTimeFilteredByType(range, type.type());
        Optional<KpiMeanValue> touchTime = this.useCase.getMeanTouchTimeFilteredByType(range, type.type());
        Optional<KpiMeanValue> valueAddedTime = this.useCase.getValueAddedMeanTimeFilteredByType(range, type.type());
        if(chirTime.isEmpty() || anestTime.isEmpty() || touchTime.isEmpty() || valueAddedTime.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new SurgeriesMeanTimes(chirTime.get(), anestTime.get(), touchTime.get(), valueAddedTime.get()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getKpiValues/{kpi}/{range}")
    public HttpEntity<List<KpiDataSourceValue>> getKpiValues(@PathVariable String kpi, @PathVariable int range) {
        List<KpiDataSourceValue> values = useCase.getListKpi(range, kpi);
        return new ResponseEntity<>(values, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getSurgeryStartTimeTardinessValues/{range}")
    public HttpEntity<List<KpiMeanValueTyped>> getSurgeryStartTimeTardinessValues(@PathVariable int range) {
        Optional<List<KpiMeanValueTyped>> values = useCase.getMeanStartTimeTardinessForSurgeries(range);
        return values.<HttpEntity<List<KpiMeanValueTyped>>>map(kpiMeanValueType -> new ResponseEntity<>(kpiMeanValueType, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping("/getKpiValues/{kpi}/{range}")
    public HttpEntity<List<KpiDataSourceValue>> getKpiValues(@PathVariable String kpi, @PathVariable int range, @RequestBody SurgeryTypeRequest type) {
        List<KpiDataSourceValue> values = useCase.getListKpiFilteredByType(range, kpi, type.type());
        return new ResponseEntity<>(values, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getSurgeryTypes")
    public HttpEntity<List<String>> getSurgeryTypes() {
        return new ResponseEntity<>(useCase.getRegisteredSurgeryTypes(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getWaitingListConsistency")
    public HttpEntity<Integer> waitingListConsistency() {
        return new ResponseEntity<>(useCase.waitingListConsistency(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getPatientOverThreshold")
    public HttpEntity<Integer> getPatientOverThreshold() {
        return new ResponseEntity<>(useCase.overThresholdSurgeries(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getPatientInfo/{identifier}")
    public HttpEntity<PatientInformation> getPatientInfo(@PathVariable String identifier) {
        return new ResponseEntity<>(useCase.getPatientInfo(identifier), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getSurgeryInfo/{identifier}")
    public HttpEntity<SurgeryInformation> getSurgeryInfo(@PathVariable String identifier) {
        return new ResponseEntity<>(useCase.getSurgeryInfo(identifier), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getOperationRoomDepartment/{identifier}")
    public HttpEntity<List<String>> getOperationRoomDepartment(@PathVariable String identifier) {
        return new ResponseEntity<>(useCase.getOperationRoomDepartment(identifier), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getMeanTimeByPriority/{priority}")
    public HttpEntity<Integer> getMeanTimeByPriority(@PathVariable String priority) {
        return new ResponseEntity<>(useCase.meanWaitingTimeForPriority(priority), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getOperatingRoomMeanValues/{idRoom}/{range}")
    public HttpEntity<OperatingRoomMeanTimes> getOperatingRoomMeanValues(@PathVariable String idRoom, @PathVariable int range) {
        Optional<KpiMeanValue> optStt = useCase.getStartTimeTardiness(idRoom, range);
        Optional<KpiMeanValue> optPercentage = useCase.getSurgeriesPercentage(idRoom, range);
        if(optPercentage.isEmpty() || optStt.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(new OperatingRoomMeanTimes(optStt.get(), optPercentage.get()), HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/getOperatingRoomKpi/{kpi}/{idRoom}/{range}")
    public HttpEntity<List<KpiDataSourceValue>> getOperatingRoomKpi(@PathVariable String kpi, @PathVariable String idRoom, @PathVariable int range) {
        return new ResponseEntity<>(useCase.getOperatingRoomsKpiValues(kpi, range, idRoom), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getOperatingRoomNames")
    public HttpEntity<List<String>> getOperatingRoomNames() {
        return new ResponseEntity<>(useCase.getOperatingRoomNames(), HttpStatus.ACCEPTED);
    }



}
