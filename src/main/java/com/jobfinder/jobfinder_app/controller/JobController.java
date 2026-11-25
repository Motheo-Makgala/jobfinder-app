package com.jobfinder.jobfinder_app.controller;

import com.jobfinder.jobfinder_app.model.Job;
import com.jobfinder.jobfinder_app.service.JobSearchService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobSearchService service;

    public JobController(JobSearchService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public List<Job> search(@RequestParam(required = false) String title,
                            @RequestParam(required = false) String location,
                            @RequestParam(defaultValue = "30") int daysPosted) {
        return service.search(title, location, daysPosted);
    }
}