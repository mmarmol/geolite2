/**
 * Copyright 2016 gromit.it
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gromit.geolite2;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.exception.GeoIp2Exception;

public class GeoLocationTest {

	@Test
	public void testStartStop() throws UnknownHostException, IOException, GeoIp2Exception {
		GeoLocation reader = new GeoLocation();
		reader.start();
		try{
			reader.start();
			fail();
		}catch(IllegalStateException e){	
		}
		System.out.println(new ObjectMapper().writeValueAsString(reader.location("179.215.124.14")));
		System.out.println(new ObjectMapper().writeValueAsString(reader.location(-23.95,-46.3333)));
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
