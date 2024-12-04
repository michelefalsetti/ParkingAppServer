package com.scrum.parkingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class ParkingSpaceAppServerApplicationTests {

	@Test
	void contextLoads() {

	}

}
