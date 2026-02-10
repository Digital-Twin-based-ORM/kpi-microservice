package com.example.kpi_microservice.businessLayer.adapter;

public class SurgeriesMeanTimes {
    KpiMeanValue chirurgicMeanTime;
    KpiMeanValue anestMeanTime;
    KpiMeanValue meanTouchTime;
    KpiMeanValue valueAddedTime;

    public SurgeriesMeanTimes() {
    }

    public SurgeriesMeanTimes(KpiMeanValue chirurgicMeanTime, KpiMeanValue anestMeanTime, KpiMeanValue meanTouchTime, KpiMeanValue valueAddedTime) {
        this.chirurgicMeanTime = chirurgicMeanTime;
        this.anestMeanTime = anestMeanTime;
        this.meanTouchTime = meanTouchTime;
        this.valueAddedTime = valueAddedTime;
    }

    public KpiMeanValue getValueAddedTime() {
        return valueAddedTime;
    }

    public void setValueAddedTime(KpiMeanValue valueAddedTime) {
        this.valueAddedTime = valueAddedTime;
    }

    public KpiMeanValue getChirurgicMeanTime() {
        return chirurgicMeanTime;
    }

    public KpiMeanValue getAnestMeanTime() {
        return anestMeanTime;
    }

    public KpiMeanValue getMeanTouchTime() {
        return meanTouchTime;
    }

    public void setChirurgicMeanTime(KpiMeanValue chirurgicMeanTime) {
        this.chirurgicMeanTime = chirurgicMeanTime;
    }

    public void setAnestMeanTime(KpiMeanValue anestMeanTime) {
        this.anestMeanTime = anestMeanTime;
    }

    public void setMeanTouchTime(KpiMeanValue meanTouchTime) {
        this.meanTouchTime = meanTouchTime;
    }
}
