package be.foreseegroup.micro.resourceservice.unitsidecomposite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Kaj on 24/09/15.
 */

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class UnitSideCompositeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnitSideCompositeServiceApplication.class, args);
    }
}
