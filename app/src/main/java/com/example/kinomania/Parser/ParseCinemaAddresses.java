package com.example.kinomania.Parser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseCinemaAddresses {

    public String ParseAddress(Document document)
    {
        String list = "";
        Elements items = document.select("span.theaterInfo_dataAddr");
        for(Element item : items)
        {
            list = item.text();
        }
        return list;
    }
}
