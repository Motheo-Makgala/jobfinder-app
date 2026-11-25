package com.jobfinder.jobfinder_app.service;

import com.jobfinder.jobfinder_app.model.Job;
import com.jobfinder.jobfinder_app.provider.JobProvider;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JobSearchService {

    private final List<JobProvider> providers;
    private final Clock clock;

    public JobSearchService(List<JobProvider> providers, Clock clock) {
        this.providers = providers;
        this.clock = clock;
    }

    public List<Job> search(String title, String location, int daysPosted) {
        LocalDate cutoff = LocalDate.now(clock).minusDays(daysPosted);
        return providers.stream()
                .flatMap(p -> p.fetchJobs().stream())
                .filter(j -> matches(j.title(), title))
                .filter(j -> matches(j.location(), location))
                .filter(j -> !j.datePosted().isBefore(cutoff))
                .sorted(Comparator.comparing(Job::datePosted).reversed())
                .toList();
    }

    private boolean matches(String value, String query) {
        return query == null || query.isBlank()
                || value.toLowerCase().contains(query.toLowerCase());
    }
}