package ch.octo.blog.transport.journeybooking;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
@EnableCircuitBreaker
public class ConfigurationFeign {
}
