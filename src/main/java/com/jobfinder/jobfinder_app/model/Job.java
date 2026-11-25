package com.jobfinder.jobfinder_app.model;

import java.time.LocalDate;
import java.util.Set;

public record Job(
        String id,
        String title,
        String company,
        String location,
        LocalDate datePosted,
        String description,
        Set<String> skills) {}