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

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.maxmind.db.NodeCache;

/**
 * The Class GuavaCache.
 */
public class GuavaCache implements NodeCache {

    /** The Constant DEFAULT_CAPACITY. */
    private static final int DEFAULT_CAPACITY = 16384;
    
    /** The cache. */
    private final Cache<Integer, JsonNode> cache;

    /**
     * Instantiates a new guava cache.
     */
    public GuavaCache() {
    	cache = CacheBuilder.newBuilder().maximumSize(DEFAULT_CAPACITY).build();
    }

    /**
     * Instantiates a new guava cache.
     *
     * @param maximumSize the maximum size
     */
    public GuavaCache(int maximumSize) {
    	cache = CacheBuilder.newBuilder().maximumSize(maximumSize).build();
    }
    
    /**
     * Instantiates a new guava cache.
     *
     * @param cache the cache
     */
    public GuavaCache(Cache<Integer, JsonNode> cache){
    	this.cache = cache;
    }

    /* (non-Javadoc)
     * @see com.maxmind.db.NodeCache#get(int, com.maxmind.db.NodeCache.Loader)
     */
    @Override
    public JsonNode get(int key, Loader loader) throws IOException {
        Integer k = key;
        JsonNode value = cache.getIfPresent(k);
        if (value == null) {
            value = loader.load(key);
        }
        return value;
    }

}
