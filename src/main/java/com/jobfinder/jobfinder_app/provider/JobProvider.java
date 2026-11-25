package com.jobfinder.jobfinder_app.provider;

import com.jobfinder.jobfinder_app.model.Job;
import java.util.List;

public interface JobProvider {
    List<Job> fetchJobs();
}