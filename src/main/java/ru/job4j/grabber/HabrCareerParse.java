package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    public static void main(String[] args) throws IOException {
        for (int page = 1; page <= 5; page++) {
            String currentPage = String.format("%s%s", PAGE_LINK, page);
            Connection connection = Jsoup.connect(currentPage);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateTimeElement = row.select(".vacancy-card__date").first();
                Element dateTimeLink = dateTimeElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String dateTime = String.format("%s", dateTimeLink.attr("datetime"));
                HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
                LocalDateTime ldt = parser.parse(dateTime);
                System.out.printf("%s %s %s%n", ldt, vacancyName, link);
            });
        }
    }
}
