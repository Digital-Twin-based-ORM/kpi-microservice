package com.example.kpi_microservice;

import com.example.kpi_microservice.domain.SurgeryDate;
import com.example.kpi_microservice.utils.UtilsFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
class KpiMicroserviceApplicationTests {

	@Test
	void meanPeriodDaysTest() {
        ArrayList<SurgeryDate> dates = new ArrayList<SurgeryDate>();
        dates.add(new SurgeryDate(LocalDateTime.now().minusDays(10), LocalDateTime.now()));
        dates.add(new SurgeryDate(LocalDateTime.now().minusDays(20), LocalDateTime.now()));
        dates.add(new SurgeryDate(LocalDateTime.now().minusDays(30), LocalDateTime.now()));
        dates.add(new SurgeryDate(LocalDateTime.now().minusDays(20), LocalDateTime.now()));
        Assertions.assertEquals(20, UtilsFunctions.meanPeriodDays(dates));
	}

}
