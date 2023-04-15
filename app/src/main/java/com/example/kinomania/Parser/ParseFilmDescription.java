package com.example.kinomania.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseFilmDescription {

    public String ParseDescription(Document document)
    {
        String list = "";
        Elements items = document.select("p");
        list = items.text();
        return list;
    }
}
