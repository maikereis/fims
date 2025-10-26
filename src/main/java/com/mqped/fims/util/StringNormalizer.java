package com.mqped.fims.util;

import java.text.Normalizer;

public class StringNormalizer {

    private StringNormalizer() {
        // Utility class - prevent instantiation
    }

    /**
     * Removes diacritics (accents) from text
     * Examples: ã→a, ç→c, é→e, ô→o, á→a
     */
    public static String normalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Decompose characters into base + diacritic marks
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Remove diacritic marks (Unicode category Mn = Mark, Nonspacing)
        return normalized.replaceAll("\\p{M}", "");
    }
}