package io.gromit.geolite2.geonames;

import static org.junit.Assert.*;

import org.junit.Test;

import io.gromit.geolite2.model.Subdivision;

public class SubdivisionFinderTest {

	private SubdivisionFinder instance = new SubdivisionFinder().readLevelOne().readLevelTwo();
	
	@Test
	public void testFindStringString() {
		Subdivision sub1 = instance.find("GB","ENG");
		assertNotNull(sub1);
		System.out.println(sub1);
	}

	@Test
	public void testFindStringStringString() {
		Subdivision sub2 = instance.find("GB","ENG","F7");
		assertNotNull(sub2);
		System.out.println(sub2);
	}

}
