package com.jobfinder.jobfinder_app.service;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class SkillExtractor {

    private static final Set<String> KNOWN_SKILLS = Set.of(
            "java", "spring boot", "selenium", "playwright", "rest assured",
            "postman", "sql", "kafka", "ci/cd", "api testing", "manual testing",
            "jenkins", "docker", "python", "javascript", "typescript", "react",
            "junit", "mockito", "cucumber", "testng", "git", "maven", "agile",
            "jira", "aws", "azure", "linux", "sales", "negotiation");

    public Set<String> extract(String text) {
        if (text == null || text.isBlank()) {
            return Set.of();
        }
        String lower = text.toLowerCase();
        Set<String> found = new TreeSet<>();
        for (String skill : KNOWN_SKILLS) {
            // whole-word match, so "java" does not match inside "javascript"
            Pattern p = Pattern.compile("(?<![a-z0-9])" + Pattern.quote(skill) + "(?![a-z0-9])");
            if (p.matcher(lower).find()) {
                found.add(skill);
            }
        }
        return found;
    }
}