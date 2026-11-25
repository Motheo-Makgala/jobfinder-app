package com.jobfinder.jobfinder_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jobfinder.jobfinder_app.model.Job;
import com.jobfinder.jobfinder_app.model.MatchResult;
import com.jobfinder.jobfinder_app.provider.JobProvider;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class JobMatchServiceTest {

    private final Clock clock =
            Clock.fixed(Instant.parse("2026-11-25T10:00:00Z"), ZoneOffset.UTC);
    private final LocalDate today = LocalDate.now(clock);

    private Job job(String id, String title, Set<String> skills) {
        return new Job(id, title, "Co", "Johannesburg", today.minusDays(1), "desc", skills);
    }

    private JobMatchService serviceWith(Job... jobs) {
        JobProvider stub = () -> List.of(jobs);
        return new JobMatchService(
                new JobSearchService(List.of(stub), clock), new SkillExtractor());
    }

    @Test
    void ranksBetterMatchFirst() {
        var service = serviceWith(
                job("sales", "Sales Consultant", Set.of("sales", "negotiation")),
                job("qa", "Test Engineer", Set.of("java", "selenium", "ci/cd")));

        List<MatchResult> results = service.match("Java and Selenium tester", null, null, 30);

        assertEquals("qa", results.get(0).job().id());
        assertEquals(67, results.get(0).matchPercent());
        assertEquals(0, results.get(1).matchPercent());
    }

    @Test
    void reportsMatchedAndMissingSkills() {
        var service = serviceWith(job("1", "Dev", Set.of("java", "kafka", "spring boot")));

        MatchResult r = service.match("I know Java and Kafka", null, null, 30).get(0);

        assertEquals(Set.of("java", "kafka"), r.matchedSkills());
        assertEquals(Set.of("spring boot"), r.missingSkills());
    }

    @Test
    void jobWithNoListedSkillsScoresZero() {
        var service = serviceWith(job("1", "Mystery Role", Set.of()));
        assertEquals(0, service.match("Java", null, null, 30).get(0).matchPercent());
    }
}