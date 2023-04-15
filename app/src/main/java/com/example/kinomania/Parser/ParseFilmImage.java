package com.example.kinomania.Parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseFilmImage {
    public String ParseImage(Document document, String name)
    {
        String image_link = "";
        Elements items = document.select("img.lazy.b-loaded");
        for (Element item : items)
        {
            String value = item.attr("alt");
            if (value != null && value.contains(name))
            {
                String image = item.attr("src");
                image_link = image;
            }
        }

        return image_link;
    }
}
