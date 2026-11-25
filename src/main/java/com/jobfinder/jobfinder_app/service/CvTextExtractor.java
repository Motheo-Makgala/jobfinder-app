package com.jobfinder.jobfinder_app.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Component
public class CvTextExtractor {

    static final long MAX_BYTES = 5L * 1024 * 1024; // 5 MB

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "docx", "txt");

    public String extract(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please choose a CV file.");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("The file is too large. Maximum size is 5 MB.");
        }
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
        String ext = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : "";
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Only PDF, DOCX and TXT files are supported.");
        }
        try (InputStream in = file.getInputStream()) {
            return extract(in);
        } catch (IOException | TikaException | SAXException e) {
            throw new IllegalArgumentException("We could not read that file. Try another copy.");
        }
    }

    String extract(InputStream in) throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler(500_000); // cap extracted characters
        new AutoDetectParser().parse(in, handler, new Metadata(), new ParseContext());
        return handler.toString();
    }
}