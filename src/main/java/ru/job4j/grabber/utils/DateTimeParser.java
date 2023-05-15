package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

/**
 * DateTimeParser interface
 *
 * @author itfedorovsa (itfedorovsa@gmail.com)
 * @version 1.0
 */
public interface DateTimeParser {

    /**
     * Parse date and time
     *
     * @param parse Parse implementation. Type {@link ru.job4j.grabber.Parse>}
     * @return LocalDateTime. Type {@link java.time.LocalDateTime>}
     */
    LocalDateTime parse(String parse);

}
