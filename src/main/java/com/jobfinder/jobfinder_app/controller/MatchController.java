package com.jobfinder.jobfinder_app.controller;

import com.jobfinder.jobfinder_app.model.MatchResult;
import com.jobfinder.jobfinder_app.service.JobMatchService;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.jobfinder.jobfinder_app.service.CvTextExtractor;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final JobMatchService service;
    private final CvTextExtractor cvExtractor;

    public MatchController(JobMatchService service, CvTextExtractor cvExtractor) {
        this.service = service;
        this.cvExtractor = cvExtractor;
    }

    @PostMapping
    public List<MatchResult> match(@RequestBody MatchRequest request) {
        int days = request.daysPosted() == null ? 30 : request.daysPosted();
        return service.match(request.cvText(), request.title(), request.location(), days);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam(required = false) String title,
                                    @RequestParam(required = false) String location,
                                    @RequestParam(defaultValue = "30") int daysPosted) {
        try {
            String cvText = cvExtractor.extract(file);
            return ResponseEntity.ok(service.match(cvText, title, location, daysPosted));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}