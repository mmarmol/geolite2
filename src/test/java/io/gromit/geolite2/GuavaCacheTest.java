package io.gromit.geolite2;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.maxmind.db.NodeCache.Loader;

import io.gromit.geolite2.cache.GuavaCache;

public class GuavaCacheTest {

	private Loader loader = new Loader() {
		
		private JsonNodeFactory factory = new JsonNodeFactory(true);
		
		@Override
		public JsonNode load(int key) throws IOException {
			return factory.objectNode().put("key", key);
		}
	};
	
	@Test
	public void testGet() throws IOException {
		GuavaCache cache = new GuavaCache();
		assertEquals(1,cache.get(1, loader).get("key").asInt());
	}

}
