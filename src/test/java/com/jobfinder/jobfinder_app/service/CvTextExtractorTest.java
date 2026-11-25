package com.jobfinder.jobfinder_app.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class CvTextExtractorTest {

    private final CvTextExtractor extractor = new CvTextExtractor();

    @Test
    void extractsTextFromTxtFile() {
        var file = new MockMultipartFile("file", "cv.txt", "text/plain",
                "Java and Selenium tester".getBytes(StandardCharsets.UTF_8));
        assertTrue(extractor.extract(file).contains("Selenium"));
    }

    @Test
    void rejectsUnsupportedFileType() {
        var file = new MockMultipartFile("file", "cv.exe", "application/octet-stream", new byte[]{1, 2, 3});
        assertThrows(IllegalArgumentException.class, () -> extractor.extract(file));
    }

    @Test
    void rejectsEmptyFile() {
        var file = new MockMultipartFile("file", "cv.pdf", "application/pdf", new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> extractor.extract(file));
    }

    @Test
    void rejectsOversizedFile() {
        var file = new MockMultipartFile("file", "cv.pdf", "application/pdf",
                new byte[(int) CvTextExtractor.MAX_BYTES + 1]);
        assertThrows(IllegalArgumentException.class, () -> extractor.extract(file));
    }
}