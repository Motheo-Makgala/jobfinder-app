package com.jobfinder.jobfinder_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jobfinder.jobfinder_app.model.Job;
import com.jobfinder.jobfinder_app.provider.JobProvider;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class JobSearchServiceTest {

    private final Clock clock =
            Clock.fixed(Instant.parse("2026-11-25T10:00:00Z"), ZoneOffset.UTC);
    private final LocalDate today = LocalDate.now(clock);

    private Job job(String id, String title, String location, int daysAgo) {
        return new Job(id, title, "Co", location, today.minusDays(daysAgo), "desc", Set.of());
    }

    private JobSearchService serviceWith(Job... jobs) {
        JobProvider stub = () -> List.of(jobs);
        return new JobSearchService(List.of(stub), clock);
    }

    @Test
    void excludesJobsOlderThanDaysPosted() {
        var service = serviceWith(job("1", "Tester", "Johannesburg", 3),
                job("2", "Tester", "Johannesburg", 10));
        var result = service.search("Tester", "Johannesburg", 7);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
    }

    @Test
    void includesJobPostedExactlyOnCutoffDay() {
        var service = serviceWith(job("1", "Tester", "Johannesburg", 7));
        assertEquals(1, service.search("Tester", "Johannesburg", 7).size());
    }

    @Test
    void filtersByLocationCaseInsensitively() {
        var service = serviceWith(job("1", "Tester", "Cape Town", 1),
                job("2", "Tester", "Durban", 1));
        var result = service.search(null, "cape town", 30);
        assertEquals(1, result.size());
    }

    @Test
    void sortsNewestFirst() {
        var service = serviceWith(job("old", "Dev", "Pretoria", 9),
                job("new", "Dev", "Pretoria", 1));
        assertEquals("new", service.search("Dev", null, 30).get(0).id());
    }
}