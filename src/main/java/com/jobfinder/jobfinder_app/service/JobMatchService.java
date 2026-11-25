package com.jobfinder.jobfinder_app.service;

import com.jobfinder.jobfinder_app.model.Job;
import com.jobfinder.jobfinder_app.model.MatchResult;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Service;

@Service
public class JobMatchService {

    private final JobSearchService searchService;
    private final SkillExtractor extractor;

    public JobMatchService(JobSearchService searchService, SkillExtractor extractor) {
        this.searchService = searchService;
        this.extractor = extractor;
    }

    public List<MatchResult> match(String cvText, String title, String location, int daysPosted) {
        Set<String> cvSkills = extractor.extract(cvText);
        return searchService.search(title, location, daysPosted).stream()
                .map(job -> score(job, cvSkills))
                .sorted(Comparator.comparingInt(MatchResult::matchPercent).reversed()
                        .thenComparing(r -> r.job().datePosted(), Comparator.reverseOrder()))
                .toList();
    }

    private MatchResult score(Job job, Set<String> cvSkills) {
        Set<String> required = job.skills();
        Set<String> matched = new TreeSet<>(required);
        matched.retainAll(cvSkills);
        Set<String> missing = new TreeSet<>(required);
        missing.removeAll(cvSkills);
        int percent = required.isEmpty() ? 0 : Math.round(100f * matched.size() / required.size());
        return new MatchResult(job, percent, matched, missing);
    }
}