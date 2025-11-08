package com.example.kpi_microservice.persistence;

import com.example.kpi_microservice.businessLayer.adapter.KpiDataSourceValue;
import com.example.kpi_microservice.businessLayer.boundary.DataSourceGateway;
import com.example.kpi_microservice.domainLayer.Kpi;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KpiRepository implements DataSourceGateway {
    private static final String DB_URL = "jdbc:mysql://localhost:6033/wldt-db";
    private static final String USER = "user_name"; // Replace with your MySQL username
    private static final String PASS = "root_password"; // Replace with your MySQL password
    private static final String SURGERIES_TABLE_NAME = "surgeries_kpi";
    private static final String OR_TABLE_NAME = "operating_rooms_kpi";

    @Override
    public ArrayList<KpiDataSourceValue> getKpiValue(Kpi kpi) {
        Connection conn = getConnection();
        String selectSQL = "SELECT " + "id" + ", " + "value" + ", " + "timestamp" + " FROM " + SURGERIES_TABLE_NAME + " WHERE kpi = \'" + kpi.getKpiName() + "\';";

        ArrayList<KpiDataSourceValue> values = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and print data
            while (rs.next()) {
                int id = rs.getInt("id");
                float value = rs.getFloat("value");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                System.out.printf("ID: %d, value: %f, timestamp: %s",
                        id, value, timestamp.toString());
                values.add(new KpiDataSourceValue(id, value, timestamp));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return values;
    }

    @Override
    public ArrayList<KpiDataSourceValue> getOperatingRoomKpi(Kpi kpi, String operatingRoomId) {
        Connection conn = getConnection();
        String selectSQL = "SELECT " + "id" + ", " + "value" + ", " + "timestamp" + " FROM " + OR_TABLE_NAME + " WHERE kpi = \'" + kpi.getKpiName() + "\' AND operating_room_id = \'" + operatingRoomId + "\';";

        ArrayList<KpiDataSourceValue> values = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate through the result set and print data
            while (rs.next()) {
                int id = rs.getInt("id");
                float value = rs.getFloat("value");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

                System.out.printf("ID: %d, value: %f, timestamp: %s",
                        id, value, timestamp.toString());
                values.add(new KpiDataSourceValue(id, value, timestamp));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return values;
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

    public static void main(String[] args) {
        KpiRepository kpiRepository = new KpiRepository();
        List<KpiDataSourceValue> list = kpiRepository.getOperatingRoomKpi(Kpi.M10, "or_1");
        System.out.println(list);
    }
}
