/*package com.doms.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}*/

//For Debugging
/*package com.doms.inventoryservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class InventoryServiceApplication {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    private final RedisTemplate<String, Object> redisTemplate;

    public InventoryServiceApplication(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @PostConstruct
    public void checkRedisConnection() {
        System.out.println("🔹 Attempting to connect to Redis at " + redisHost + ":" + redisPort);
        try {
            // Simple ping check
            String pong = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping();
            System.out.println("✅ Redis responded with: " + pong);
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to Redis:");
            e.printStackTrace();
        }
    }
}*/

package com.doms.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}


