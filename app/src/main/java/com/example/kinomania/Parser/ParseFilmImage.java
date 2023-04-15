package com.example.kinomania.Parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseFilmImage {
    public String ParseImage(Document document, String name)
    {
        String image_link = "";
        Elements items = document.select("div.showtimesMovie_wrapper");
        for (Element item : items)
        {
            Elements it_name_film = item.select("span.showtimesMovie_name");
            if(it_name_film != null && it_name_film.contains(name)){
                String value = item.select("picture.showtimesMovie_poster.picture.picture-poster")
                        .select("img")
                        .attr("src");
                image_link = value;
                break;
            }
        }
        return image_link;
    }
}
