package com.example.kpi_microservice.persistence;

import com.example.kpi_microservice.domain.PatientInformation;
import com.example.kpi_microservice.domain.PriorityClass;
import com.example.kpi_microservice.domain.SurgeryDate;
import com.example.kpi_microservice.domain.SurgeryInformation;
import com.example.kpi_microservice.utils.UtilsFunctions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RemoteSparqlRepository {

    private final String host;
    private final RestClient restClient;

    public RemoteSparqlRepository(String host) {
        this.host = host;
        this.restClient = RestClient.builder()
                .baseUrl(host)
                .defaultHeader("Content-Type", "application/sparql-query")
                .build();
    }

    public SurgeryInformation getSurgeryInfo(String surgeryId) {
        try {
            String query = """
                    PREFIX fhir: <http://www.hl7.org/fhir/>
                    PREFIX mao: <https://purl.org/mao/onto/>
                    
                    SELECT ?reasonValue ?categoryValue ?statusValue ?isDone ?waitingListDate ?priority ?executionDate ?categoryCodeValue ?codeValue
                    WHERE {
                        ?surgery a fhir:Procedure;
                        fhir:reason ?reason;
                        fhir:category ?category;
                        fhir:code ?code;
                        fhir:status ?status;
                        mao:isDone ?isDone;
                        mao:waitingListDate ?waitingListDate;
                        mao:priority ?priority;
                        mao:executionDate ?executionDate;
                        fhir:identifier ?identifier.
                    
                        ?reason fhir:concept ?reasonConcept.
                        ?reasonConcept fhir:text ?reasonText.
                        ?reasonText fhir:v ?reasonValue.
                    
                        ?category fhir:text ?categoryText.
                        ?categoryText fhir:v ?categoryValue.
                    
                        ?category fhir:coding ?categoryCoding.
                        ?categoryCoding fhir:system ?categorySystem.
                        ?categorySystem fhir:v ?categoryCodeValue.
                    
                        ?code fhir:text ?codeText.
                        ?codeText fhir:v ?codeValue.
                    
                        ?status fhir:v ?statusValue.
                    
                        ?identifier fhir:value ?identifierValue.
                        ?identifierValue fhir:v '%s'.
                    }
                    """;

            String response = restClient
                    .post().uri("/wodt/sparql").accept(MediaType.APPLICATION_JSON).body(String.format(query, surgeryId)).retrieve().toEntity(String.class).getBody();

            JSONObject json = new JSONObject(response);
            JSONArray jsonArray = json.getJSONObject("results").getJSONArray("bindings");
            if(!jsonArray.isEmpty()){
                JSONObject element = jsonArray.getJSONObject(0);
                return new SurgeryInformation(
                        element.getJSONObject("reasonValue").getString("value"),
                        element.getJSONObject("categoryValue").getString("value"),
                        element.getJSONObject("categoryCodeValue").getString("value"),
                        element.getJSONObject("priority").getString("value"),
                        element.getJSONObject("executionDate").getString("value"),
                        element.getJSONObject("waitingListDate").getString("value"),
                        element.getJSONObject("isDone").getString("value"),
                        element.getJSONObject("statusValue").getString("value"),
                        element.getJSONObject("codeValue").getString("value")
                );
            } else {
                throw new IllegalArgumentException("Sparql query failed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public PatientInformation getPatientInfo(String identificationNumber) {
        try {
            String query = """
                    PREFIX fhir: <http://www.hl7.org/fhir/>
                    PREFIX mao: <https://purl.org/mao/onto/>
                    
                    SELECT ?patient ?name ?gender ?birthDate
                    WHERE {
                        ?patient a fhir:Patient;
                        fhir:name ?completeName;
                        fhir:identifier ?identifier;
                        fhir:gender ?gender;
                        fhir:birthDate ?birthDate .
                    
                        ?identifier fhir:value ?identifierValue.
                        ?identifierValue fhir:v '%s'.
                    
                        ?completeName fhir:given ?nameObject.
                        ?nameObject fhir:v ?name.
                    }
                    """;

            String response = restClient
                    .post().uri("/wodt/sparql").accept(MediaType.APPLICATION_JSON).body(String.format(query, identificationNumber)).retrieve().toEntity(String.class).getBody();

            JSONObject json = new JSONObject(response);
            JSONArray jsonArray = json.getJSONObject("results").getJSONArray("bindings");
            if(!jsonArray.isEmpty()){
                JSONObject element = jsonArray.getJSONObject(0);
                return new PatientInformation(element.getJSONObject("name").getString("value"), element.getJSONObject("gender").getString("value"), identificationNumber, element.getJSONObject("birthDate").getString("value"));
            } else {
                return new PatientInformation("","","", "");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<String> getOperationRoomDepartment(String identificationNumber) {
        try {
            System.out.println(identificationNumber);
            String query = """
                    PREFIX fhir: <http://www.hl7.org/fhir/>
                    PREFIX mao: <https://purl.org/mao/onto/>
                    
                    SELECT ?partOf
                    WHERE {
                        ?operatingroom a fhir:Location;
                        fhir:partOf ?partOf;
                        fhir:name ?name.
                    
                        ?name fhir:v '%s'.
                    }
                    """;
            String finalQuery = String.format(query, identificationNumber);
            System.out.println(finalQuery);
            String response = restClient
                    .post().uri("/wodt/sparql").accept(MediaType.APPLICATION_JSON).body(finalQuery).retrieve().toEntity(String.class).getBody();

            JSONObject json = new JSONObject(response);
            JSONArray jsonArray = json.getJSONObject("results").getJSONArray("bindings");
            System.out.println(jsonArray.toString());
            if(!jsonArray.isEmpty()){
                List<String> uris = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject element = jsonArray.getJSONObject(i);
                    uris.add(element.getJSONObject("partOf").getString("value"));
                }

                return uris;
            } else {
                return List.of();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public int getWaitingListSize() {
        try {
            String query = """
                    PREFIX fhir: <http://www.hl7.org/fhir/>
                    PREFIX mao: <https://purl.org/mao/onto/>
            
                    SELECT ?surgery
                    WHERE {
                        ?surgery a fhir:Procedure;
                        mao:isDone false .
                    }
                    """;
            String response = restClient
                    .post().uri("/wodt/sparql").accept(MediaType.APPLICATION_JSON).body(query).retrieve().toEntity(String.class).getBody();

            JSONObject json = new JSONObject(response);
            return json.getJSONObject("results").getJSONArray("bindings").toList().size();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getPatientOverThreshold() {
        try {
            String query = """
                    PREFIX fhir: <http://www.hl7.org/fhir/>
                    PREFIX mao: <https://purl.org/mao/onto/>
                    
                    SELECT ?surgery ?date ?priority
                    WHERE {
                        ?surgery a fhir:Procedure;
                        mao:isDone false ;
                        mao:waitingListDate ?date ;
                        mao:priority ?priority .
                    }
                    """;
            String response = restClient
                    .post().uri("/wodt/sparql").accept(MediaType.APPLICATION_JSON).body(query).retrieve().toEntity(String.class).getBody();

            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONObject("results").getJSONArray("bindings");
            AtomicInteger count = new AtomicInteger();
            array.forEach(obj -> {
                JSONObject jsonObj = (JSONObject) obj;
                LocalDateTime date = LocalDateTime.parse(jsonObj.getJSONObject("date").getString("value"));
                PriorityClass priority = PriorityClass.valueOf(jsonObj.getJSONObject("priority").getString("value"));
                System.out.println(date);
                if (priority == PriorityClass.D) {
                    if (Period.between(date.toLocalDate(), LocalDate.now()).getMonths() > priority.getMaxTime()) {
                        count.getAndIncrement();
                    }
                } else {
                    if (Duration.between(date, LocalDateTime.now()).toDays() > priority.getMaxTime()) {
                        count.getAndIncrement();
                    }
                }
            });
            return count.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int getMeanTimeByPriority(String priority) {
        try {
            String query = """
                    PREFIX fhir: <http://www.hl7.org/fhir/>
                    PREFIX mao: <https://purl.org/mao/onto/>
                    
                    SELECT ?surgery ?date ?executionDate
                    WHERE {
                        ?surgery a fhir:Procedure;
                        mao:isDone true ;
                        mao:priority '%s' ;
                        mao:waitingListDate ?date ;
                        mao:executionDate ?executionDate .
                    }
                    """;
            String queryWithParams = query.formatted(priority);
            System.out.println(queryWithParams);
            String response = restClient
                    .post().uri("/wodt/sparql").accept(MediaType.APPLICATION_JSON).body(query).retrieve().toEntity(String.class).getBody();

            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONObject("results").getJSONArray("bindings");
            ArrayList<SurgeryDate>  dates = new ArrayList<>();
            array.forEach(obj -> {
                JSONObject jsonObj = (JSONObject) obj;
                LocalDateTime insertionDate = LocalDateTime.parse(jsonObj.getJSONObject("date").getString("value"));
                LocalDateTime executionDate = LocalDateTime.parse(jsonObj.getJSONObject("executionDate").getString("value"));
                dates.add(new SurgeryDate(insertionDate, executionDate));
            });

            return Math.toIntExact(UtilsFunctions.meanPeriodDays(dates));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static void main(String[] args) {
        int v = new RemoteSparqlRepository("http://localhost:4000").getPatientOverThreshold();
        System.out.println(v);
    }
}
