package com.example.kinomania.Parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.util.List;


public class ParseCinemaNames {
    public List<String> ParseName(Document doc)
    {
        List<String> list = null;
        Elements items = doc.select("div.cinemaList_name");
        for (Element item : items)
        {
            list.add(item.text());
        }
        return list;
    }

    public static List<String> ParseUrls(Document doc)
    {
        List<String> list = null;
        Elements items = doc.select("a.cinemaList_ref");
        for (Element item : items)
        {
            String value = item.attr("href");
            list.add(value);
        }
        return list;
    }
}
