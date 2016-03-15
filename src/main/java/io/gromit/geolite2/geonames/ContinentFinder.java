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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.gromit.geolite2.model.Continent;

/**
 * The Class ContinentFinder.
 */
public class ContinentFinder {

	/** The iso map. */
	private Map<String, Continent> isoMap = new HashMap<>();
	
	/**
	 * Instantiates a new continent finder.
	 */
	public ContinentFinder(){
		isoMap.put("AF", new Continent(6255146, "AF", "Africa"));
		isoMap.put("AS", new Continent(6255147, "AS", "Asia"));
		isoMap.put("EU", new Continent(6255148, "EU", "Europe"));
		isoMap.put("NA", new Continent(6255149, "NA", "North America"));
		isoMap.put("OC", new Continent(6255151, "OC", "Oceania"));
		isoMap.put("SA", new Continent(6255150, "SA", "South America"));
		isoMap.put("AN", new Continent(6255152, "AN", "Antarctica"));
	}

	/**
	 * Find.
	 *
	 * @param iso the iso
	 * @return the continent
	 */
	public Continent find(String iso){
		if(StringUtils.isBlank(iso)){
			return null;
		}
		return isoMap.get(iso);
	}
	
}
