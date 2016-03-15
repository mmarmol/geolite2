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
