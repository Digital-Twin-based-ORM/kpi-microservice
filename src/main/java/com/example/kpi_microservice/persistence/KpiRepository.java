package com.example.kpi_microservice.persistence;

import com.example.kpi_microservice.businessLayer.adapter.KpiDataSourceValue;
import com.example.kpi_microservice.businessLayer.boundary.DataSourceGateway;
import com.example.kpi_microservice.domain.PatientInformation;
import com.example.kpi_microservice.domain.SurgeryInformation;
import com.example.kpi_microservice.domainLayer.Kpi;
import com.example.kpi_microservice.utils.EnvironmentVariable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KpiRepository implements DataSourceGateway {
    private static final String DB_URL = "jdbc:mysql://" + EnvironmentVariable.DB_HOST_ENV.getValue() + ":" + EnvironmentVariable.DB_PORT_ENV.getValue() + "/wldt-db";
    private static final String USER = "user_name"; // Replace with your MySQL username
    private static final String PASS = "root_password"; // Replace with your MySQL password
    private static final String SURGERIES_TABLE_NAME = "surgeries_kpi";
    private static final String OR_TABLE_NAME = "operating_rooms_kpi";
    private static final String ALL_SURGERIES = "simulation_table";

    private RemoteSparqlRepository remoteRepository;

    public KpiRepository(RemoteSparqlRepository remoteRepository) {
        this.remoteRepository = remoteRepository;
    }

    @Override
    public ArrayList<KpiDataSourceValue> getSurgeryKpiValue(Kpi kpi, int daysRange) {
        String selectSQL = "SELECT " + "id" + ", " + "value" + ", " + "timestamp" + " FROM " + SURGERIES_TABLE_NAME + " WHERE kpi = \'" + kpi.getKpiName() + "\';";
        return getKpiValues(selectSQL, daysRange);
    }

    @Override
    public ArrayList<KpiDataSourceValue> getOperatingRoomsKpiValue(Kpi kpi, int daysRange, String orName) {
        String selectSQL = "SELECT " + "id" + ", " + "value" + ", " + "timestamp" + " FROM " + OR_TABLE_NAME + " WHERE kpi = \'" + kpi.getKpiName() + "\' AND operating_room_id = \'" + orName + "\';";
        ArrayList<KpiDataSourceValue> values = getKpiValues(selectSQL, daysRange);
        System.out.println("VALUES: " + values.size());
        return values;
    }

    private ArrayList<KpiDataSourceValue> getKpiValues(String selectSQL, int daysRange) {
        Connection conn = getConnection();

        LocalDateTime firstOfMonth = LocalDateTime.now().minusDays(daysRange);
        ArrayList<KpiDataSourceValue> values = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and print data
            while (rs.next()) {
                int id = rs.getInt("id");
                float value = rs.getFloat("value");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                if(timestamp.isAfter(firstOfMonth)) {
                    System.out.printf("ID: %d, value: %f, timestamp: %s",
                            id, value, timestamp.toString());
                    values.add(new KpiDataSourceValue(id, value, timestamp));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return values;
    }

    @Override
    public List<String> getRegisteredSurgeryTypes() {
        Connection conn = getConnection();
        String selectSQL = "SELECT DISTINCT " + "surgeryType" + " FROM " + SURGERIES_TABLE_NAME;

        ArrayList<String> types = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and print data
            while (rs.next()) {
                String type = rs.getString("surgeryType");

                types.add(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return types;
    }

    @Override
    public List<String> getOperatingRoomNames() {
        Connection conn = getConnection();
        String selectSQL = "SELECT DISTINCT " + "operating_room_id" + " FROM " + OR_TABLE_NAME;

        ArrayList<String> types = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and print data
            while (rs.next()) {
                String type = rs.getString("operating_room_id");

                types.add(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return types;
    }

    @Override
    public List<KpiDataSourceValue> getListKpi(int daysRange, String kpi) {
        Connection conn = getConnection();
        //SELECT * FROM `surgeries_kpi` WHERE kpi = "M14"
        String selectSQL = "SELECT * " + " FROM " + SURGERIES_TABLE_NAME + " WHERE kpi = '" + kpi + "'";

        ArrayList<KpiDataSourceValue> records = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            LocalDateTime firstOfMonth = LocalDateTime.now().minusDays(daysRange);

            // Iterate through the result set and print data
            while (rs.next()) {
                float value = rs.getFloat("value");
                int id = rs.getInt("id");
                LocalDateTime localDateTime =  rs.getTimestamp("timestamp").toLocalDateTime();
                if(localDateTime.isAfter(firstOfMonth)) {
                    KpiDataSourceValue record = new KpiDataSourceValue(id, value, localDateTime);
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    @Override
    public List<KpiDataSourceValue> getListKpiFilteredByType(int daysRange, String kpi, String type) {
        Connection conn = getConnection();
        type = type.replace("'", "\\'");
        System.out.println(type);
        String selectSQL = "SELECT * " + " FROM " + SURGERIES_TABLE_NAME + " WHERE kpi = '" + kpi + "' AND surgeryType = '" + type + "'";

        ArrayList<KpiDataSourceValue> records = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            LocalDateTime firstOfMonth = LocalDateTime.now().minusDays(daysRange);

            // Iterate through the result set and print data
            while (rs.next()) {
                float value = rs.getFloat("value");
                int id = rs.getInt("id");
                LocalDateTime localDateTime =  rs.getTimestamp("timestamp").toLocalDateTime();
                if(localDateTime.isAfter(firstOfMonth)) {
                    KpiDataSourceValue record = new KpiDataSourceValue(id, value, localDateTime);
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return records;
    }

    @Override
    public PatientInformation getPatientInfo(String identifier) {
        return this.remoteRepository.getPatientInfo(identifier);
    }

    @Override
    public int waitingListConsistency() {
        return this.remoteRepository.getWaitingListSize();
    }

    @Override
    public int overThresholdSurgeries() {
        return this.remoteRepository.getPatientOverThreshold();
    }

    @Override
    public int meanWaitingTimeForPriority(String priority) {
        return this.remoteRepository.getMeanTimeByPriority(priority);
    }

    private static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. Register the JDBC driver (not strictly necessary for modern JDBC, but good practice)
            // Class.forName("com.mysql.cj.jdbc.Driver"); // For MySQL 8.x and later

            System.out.println("Connecting to database...");
            // 2. Open a connection
            return conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SurgeryInformation getSurgeryInfo(String identifier) {
        return this.remoteRepository.getSurgeryInfo(identifier);
    }

    @Override
    public List<String> getOperationRoomDepartment(String orName) {
        return this.remoteRepository.getOperationRoomDepartment(orName);
    }
}
