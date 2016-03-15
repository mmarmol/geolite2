package io.gromit.geolite2.geonames;

import static org.junit.Assert.*;

import org.junit.Test;

import io.gromit.geolite2.model.Country;

public class CountryFinderTest {

	private CountryFinder instance = new CountryFinder().readCountries();
	
	@Test
	public void testFind() {
		Country country = instance.find(1149361);
		assertEquals("Afghanistan", country.getName());
		System.out.println(country);
	}

}
