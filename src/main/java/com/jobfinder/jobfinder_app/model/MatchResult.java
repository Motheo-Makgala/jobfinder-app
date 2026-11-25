package com.jobfinder.jobfinder_app.model;

import java.util.Set;

public record MatchResult(
        Job job,
        int matchPercent,
        Set<String> matchedSkills,
        Set<String> missingSkills) {}