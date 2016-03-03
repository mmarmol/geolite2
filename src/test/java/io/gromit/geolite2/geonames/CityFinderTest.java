package io.gromit.geolite2.geonames;

import static org.junit.Assert.*;

import org.junit.Test;

public class CityFinderTest {

	private CityFinder instance = new CityFinder().readCities();
	
	@Test
	public void findByGeonameId() {
		City city = instance.find(2643743);
		assertEquals("London", city.getName());
		System.out.println(city);
	}
	
	@Test
	public void findByLongLat(){
		City city = instance.find(Double.valueOf(-0.0941),Double.valueOf(51.5144));
		assertEquals(Integer.valueOf(2643741), city.getGeonameId());
		System.out.println(city);
	}

}
