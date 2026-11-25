package com.jobfinder.jobfinder_app.controller;

import com.jobfinder.jobfinder_app.model.MatchResult;
import com.jobfinder.jobfinder_app.service.JobMatchService;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final JobMatchService service;

    public MatchController(JobMatchService service) {
        this.service = service;
    }

    @PostMapping
    public List<MatchResult> match(@RequestBody MatchRequest request) {
        int days = request.daysPosted() == null ? 30 : request.daysPosted();
        return service.match(request.cvText(), request.title(), request.location(), days);
    }
}