package com.example.kpi_microservice.controller;

import com.example.kpi_microservice.businessLayer.boundary.InputBoundary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ServiceController {

    private final InputBoundary useCase;

    public ServiceController(InputBoundary useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/test")
    public HttpEntity<String> test() {
        return new ResponseEntity<>("OK", HttpStatus.FOUND);
    }

}
