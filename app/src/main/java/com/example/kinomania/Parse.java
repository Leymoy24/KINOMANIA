package com.example.kinomania;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.kinomania.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Parse extends AsyncTask<Void, Void, Void> {
    public String BaseUrl = "https://msk.kinoafisha.info/cinema/";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Document doc = Jsoup.connect(BaseUrl).get();


            /*//Get the logo source of the website
            Element img = document.attr("img").first();
            // Locate the src attribute
            String imgSrc = img.absUrl("src");
            // Download image from URL
            InputStream input = new URL(imgSrc).openStream();
            // Decode Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            //Get the title of the website
            String title = document.title();*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
