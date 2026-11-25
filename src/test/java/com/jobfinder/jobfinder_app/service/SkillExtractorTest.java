package com.jobfinder.jobfinder_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;

class SkillExtractorTest {

    private final SkillExtractor extractor = new SkillExtractor();

    @Test
    void findsSkillsCaseInsensitively() {
        var skills = extractor.extract("Experienced with JAVA, Selenium and Spring Boot.");
        assertEquals(Set.of("java", "selenium", "spring boot"), skills);
    }

    @Test
    void doesNotMatchJavaInsideJavaScript() {
        var skills = extractor.extract("Frontend work in JavaScript only.");
        assertEquals(Set.of("javascript"), skills);
    }

    @Test
    void handlesSlashedSkillNames() {
        assertTrue(extractor.extract("Built CI/CD pipelines").contains("ci/cd"));
    }

    @Test
    void blankOrNullTextGivesNoSkills() {
        assertTrue(extractor.extract("   ").isEmpty());
        assertTrue(extractor.extract(null).isEmpty());
    }
}