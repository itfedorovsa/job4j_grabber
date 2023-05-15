package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HabrCareer DateTimeParser implementation
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public class HabrCareerDateTimeParser implements DateTimeParser {

    /**
     * Parse date and time
     *
     * @param parse Parse implementation. Type {@link ru.job4j.grabber.Parse>}
     * @return LocalDateTime. Type {@link java.time.LocalDateTime>}
     */
    @Override
    public LocalDateTime parse(String parse) {
        /*return ZonedDateTime.parse(parse).toLocalDateTime();*/
        return LocalDateTime.parse(parse, DateTimeFormatter.ISO_DATE_TIME);
    }

}
