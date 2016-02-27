package io.gromit.geolite2;

import static org.junit.Assert.*;

import org.junit.Test;

public class ScheduledDatabaseReaderTest {

	@Test
	public void testStartStop() {
		ScheduledDatabaseReader reader = new ScheduledDatabaseReader();
		assertNotNull(reader.start().databaseReader());
		try{
			reader.start();
			fail();
		}catch(IllegalStateException e){	
		}
		reader.stop();
	}

	@Test
	public void testStop() {
		try{
			new ScheduledDatabaseReader().stop();
			fail();
		}catch(IllegalStateException e){	
		}
	}

}
