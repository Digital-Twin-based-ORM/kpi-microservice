package com.example.kpi_microservice.utils;

import com.example.kpi_microservice.domain.SurgeryDate;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UtilsFunctions {

    public static long meanPeriodDays(List<SurgeryDate> dates) {
        if(dates.isEmpty()){ return 0; }

        AtomicLong count = new AtomicLong();
        dates.forEach(date -> {
            long duration = ChronoUnit.DAYS.between(date.insertionDate().toLocalDate(), date.executionDate().toLocalDate());
            count.accumulateAndGet(duration, Math::addExact);
        });
        return count.get() / dates.size();
    }

}
