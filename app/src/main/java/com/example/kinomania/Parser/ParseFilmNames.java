package com.example.kinomania.Parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;

public class ParseFilmNames {
    public List<String> ParseName(Document document)
    {
        List<String> list = null;
        Elements items = document.select("span.showtimesMovie_name");
        for(Element item : items)
        {
            list.add(item.text());
        }
        return list;
    }
}
