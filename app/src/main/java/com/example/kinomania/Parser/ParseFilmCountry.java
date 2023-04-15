package com.example.kinomania.Parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class ParseFilmCountry {

    public List<String> ParseCountry(Document document)
    {
        List<String> list = null;
        Elements items = document.select("span.showtimesMovie_details");
        for(Element item : items)
        {
            list.add(item.text());
        }
        return list;
    }
}
