package io.gromit.geolite2.geonames;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeZoneFinderTest {

	@Test
	public void testFind() {
		assertNotNull(new TimeZoneFinder().readTimeZones().find("America/Argentina/Buenos_Aires"));
	}

}
