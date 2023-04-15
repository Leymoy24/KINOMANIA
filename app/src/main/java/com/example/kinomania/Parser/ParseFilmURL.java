package com.example.kinomania.Parser;

import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseFilmURL {
    public List<String> ParseUrlOfFilm(Document document)
    {
        List<String> list = null;
        Elements items = document.select("a.showtimesMovie_link");
        for (Element item : items)
        {
            String value = item.attr("href");
            list.add(value);
        }
        return list;
    }
}
