package com.springcrud.config;


import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

@Configuration
public class ProductCacheConfig {
		@Bean
		public Config catchCofig() {
			return new Config()
					.setInstanceName("hazel-instance")
					.addMapConfig(new MapConfig()
							.setName("product-cache")
							.setTimeToLiveSeconds(3000));
				
		}
	    // Step 2: Create HazelcastInstance ← THIS WAS MISSING
	    @Bean
	    public HazelcastInstance hazelcastInstance(Config hazelcastConfig) {
	        return Hazelcast.newHazelcastInstance(hazelcastConfig);
	    }

	    // Step 3: Create CacheManager ← THIS WAS MISSING
	    @Bean
	    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
	        return new HazelcastCacheManager(hazelcastInstance);
	    }

	
}
