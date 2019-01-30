package com.example.demo.controller;

import java.util.function.Supplier;

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
public class TestEndpointController implements Supplier<Number> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEndpointController.class);
    // NO THREAD SAFE IMPLEMENTATION!!!
    private static long previousCallTime;
    private static long lastCallTime;

    private final Counter myCounter;
    private final Gauge myGauge;

    public TestEndpointController(MeterRegistry registry) {
        this.myCounter = Counter.builder("mycustomcounter").baseUnit("count").tag("env", "dev").description("custom counter")
                .register(registry);
        this.myGauge = Gauge.builder("timesincelastcall", this).baseUnit("seconds").tag("env", "dev").description("custom gauge in seconds")
                .register(registry);
    }

    @Timed
    @GetMapping("/")
    ResponseEntity<?> getAccessToHttpsApp() {
        previousCallTime = lastCallTime;
        lastCallTime = System.currentTimeMillis();
        LOGGER.info("API endpoint of https-only-test-app had been accessed!");

        this.myCounter.increment();
        this.myGauge.value();

        return new ResponseEntity<>("https-only-test-app reached", HttpStatus.OK);
    }

    @Override
    public Number get() {
        if (previousCallTime == 0) {
            return 0d;
        }
        long millisSince = lastCallTime - previousCallTime;
        Number returnValue = millisSince / 1000d;
        return returnValue;
    }
}
