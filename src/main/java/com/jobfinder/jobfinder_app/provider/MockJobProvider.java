package com.jobfinder.jobfinder_app.provider;

import com.jobfinder.jobfinder_app.model.Job;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class MockJobProvider implements JobProvider {

    private final Clock clock;

    public MockJobProvider(Clock clock) {
        this.clock = clock;
    }

    @Override
    public List<Job> fetchJobs() {
        LocalDate today = LocalDate.now(clock);
        return List.of(
                new Job("m-1", "Test Automation Engineer", "Acme Bank", "Johannesburg",
                        today.minusDays(1), "Selenium, Java and CI/CD for banking apps.",
                        Set.of("java", "selenium", "ci/cd")),
                new Job("m-2", "Java Developer", "SafeCover Insurance", "Cape Town",
                        today.minusDays(5), "Spring Boot microservices for claims.",
                        Set.of("java", "spring boot", "kafka")),
                new Job("m-3", "QA Analyst", "PayFast Labs", "Pretoria",
                        today.minusDays(12), "Manual and API testing.",
                        Set.of("postman", "sql", "api testing")),
                new Job("m-4", "Senior SDET", "CloudPay", "Remote",
                        today.minusDays(20), "Playwright, REST Assured and pipelines.",
                        Set.of("java", "playwright", "rest assured")),
                new Job("m-5", "Sales Consultant", "RetailCo", "Durban",
                        today.minusDays(2), "Retail sales role.",
                        Set.of("sales", "negotiation"))
        );
    }
}