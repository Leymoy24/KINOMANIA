package com.example.kinomania.Parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class ParseFilmSessions {
    public List<String> ParseSessions(Document document)
    {
        List<String> list = null;
        Elements items = document.select("div.[data-schedulesearch-item*=''.span.session_time");
        for(Element item : items)
        {
            list.add(item.text());
        }
        return list;
    }
}
