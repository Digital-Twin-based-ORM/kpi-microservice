package com.example.kpi_microservice.utils;

public class EnvironmentVariable {

    private String key;
    private String defaultValue;
    private String value = "";

    private static final String DB_HOST = "DB_HOST";
    private static final String DB_PORT = "DB_PORT";
    private static final String DB_NAME = "DB_NAME";
    private static final String DB_USER = "DB_USER";
    private static final String DB_PASS = "DB_PASS";
    private static final String WODT_HOST = "WODT_HOST";
    private static final String WODT_PORT = "WODT_PORT";

    public static final EnvironmentVariable DB_HOST_ENV = new EnvironmentVariable(DB_HOST, "localhost");
    public static final EnvironmentVariable DB_NAME_ENV = new EnvironmentVariable(DB_NAME, "user_name");
    public static final EnvironmentVariable DB_USER_ENV = new EnvironmentVariable(DB_USER, "root_password");
    public static final EnvironmentVariable DB_PASS_ENV = new EnvironmentVariable(DB_PASS, "localhost");
    public static final EnvironmentVariable DB_PORT_ENV = new EnvironmentVariable(DB_PORT, "6033");
    public static final EnvironmentVariable WODT_HOST_ENV = new EnvironmentVariable(WODT_HOST, "localhost");
    public static final EnvironmentVariable WODT_PORT_ENV = new EnvironmentVariable(WODT_PORT, "4000");

    public EnvironmentVariable(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = System.getenv(key);
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        if(value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

}
