package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    private final DateTimeParser dateTimeParser;

    public static final int PAGE_COUNT = 5;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) {
        try {
            Connection connection = Jsoup.connect(link);
            Document document = connection.get();
            Element desc = document.select(".collapsible-description__content").first();
            return desc.text();
        } catch (IOException e) {
            throw new IllegalArgumentException("Something is wrong.");
        }
    }

    private Post parsePost(Element element) {
        Element titleElement = element.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        Element dateTimeElement = element.select(".vacancy-card__date").first();
        Element dateTimeLink = dateTimeElement.child(0);
        String vacancyName = titleElement.text();
        String postLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        String dateTime = String.format("%s", dateTimeLink.attr("datetime"));
        LocalDateTime ldt = dateTimeParser.parse(dateTime);
        String description = retrieveDescription(postLink);
        return new Post(vacancyName, postLink, description, ldt);
    }

    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();
        try {
            for (int page = 1; page <= PAGE_COUNT; page++) {
                String currentPage = String.format("%s%s", link, page);
                Connection connection = Jsoup.connect(currentPage);
                Document document = connection.get();
                Elements rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> list.add(parsePost(row)));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Something is wrong.");
        }
        return list;
    }

    public static void main(String[] args) {
        HabrCareerParse parser = new HabrCareerParse(new HabrCareerDateTimeParser());
        List<Post> list = parser.list(PAGE_LINK);
        for (Post l : list) {
            System.out.println(l);
        }
    }
}
