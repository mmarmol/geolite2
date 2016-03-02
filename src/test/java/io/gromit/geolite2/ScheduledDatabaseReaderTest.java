package io.gromit.geolite2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;

public class ScheduledDatabaseReaderTest {

	@Test
	public void testStartStop() throws UnknownHostException, IOException, GeoIp2Exception {
		GeoLocation reader = new GeoLocation();
		reader.start();
		try{
			reader.start();
			fail();
		}catch(IllegalStateException e){	
		}
		System.out.println(new ObjectMapper().writeValueAsString(reader.location("213.199.154.23")));
		
		reader.stop();
	}

	@Test
	public void testStop() {
		try{
			new GeoLocation().stop();
			fail();
		}catch(IllegalStateException e){	
		}
	}

}
