package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class TestEndpointController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEndpointController.class);
    // NO THREAD SAFE IMPLEMENTATION!!!
    private static long previousCallTime;
    private static long lastCallTime;

    private final Counter myCounter;
    private final Gauge myPreviousSinceLastCallGauge;
    private final Gauge mySinceLastCallGauge;

    public TestEndpointController(MeterRegistry registry) {
        this.myCounter = Counter.builder("mycounter").baseUnit("count").tag("env", "dev").description("This is my counter")
                .register(registry);
        this.mySinceLastCallGauge = Gauge.builder("mySinceLastCall", this, TestEndpointController::calcSinceLastCallTime)
                .baseUnit("seconds").tag("env", "dev").description("This is the time since last-call").register(registry);
        this.myPreviousSinceLastCallGauge = Gauge
                .builder("myPreviousSinceLastCall", this, TestEndpointController::calcPreviousSinceLastCallTime).baseUnit("seconds")
                .tag("env", "dev").description("This is the time of last-call minus previous-call").register(registry);
    }

    @Timed
    @GetMapping("/")
    ResponseEntity<?> getAccessToHttpsApp() {
        previousCallTime = lastCallTime;
        lastCallTime = System.currentTimeMillis();
        LOGGER.info("API endpoint of https-only-test-app had been accessed!");

        this.myCounter.increment();
        this.myPreviousSinceLastCallGauge.value();
        this.mySinceLastCallGauge.value();

        return new ResponseEntity<>("https-only-test-app reached", HttpStatus.OK);
    }

    public Double calcSinceLastCallTime() {
        if (lastCallTime == 0) {
            return 0d;
        }
        return (System.currentTimeMillis() - lastCallTime) / 1000d;
    }

    public Double calcPreviousSinceLastCallTime() {
        if (previousCallTime == 0) {
            return 0d;
        }
        return (lastCallTime - previousCallTime) / 1000d;
    }
}
