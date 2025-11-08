package com.example.kpi_microservice.domainLayer;

public enum Kpi {

    M1("m1"),
    M2("m2"),
    M3("m3"),
    M9("m9"),
    M10("m10"),
    M11("m11"),
    M12("m12"),
    M13("m13"),
    M14("m14"),
    M15("m15"),
    M16("m16"),
    M17("m17"),
    M18("m18"),
    M19("m19"),
    M20("m20"),
    M21("m21"),
    M22("m22"),
    M23("m23"),
    M24("m24"),
    M26("m26"),
    M29("m29");
    ;
    String name = "";

    Kpi(String name) {
        this.name = name;
    }

    public String getKpiName() {
        return name;
    }
}
