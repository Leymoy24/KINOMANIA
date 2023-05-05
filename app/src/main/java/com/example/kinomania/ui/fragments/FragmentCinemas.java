package com.example.kinomania.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinomania.data.models.Cinema;
import com.example.kinomania.ui.activities.MainActivity;
import com.example.kinomania.Parse;
import com.example.kinomania.Parser.CinemaSettings;
import com.example.kinomania.R;
import com.example.kinomania.ui.adapters.CinemasAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class FragmentCinemas extends Fragment {

    MainActivity mainActivity;
    Parse parse;
    public String BaseUrl = "https://msk.kinoafisha.info/cinema/";
    private RecyclerView recyclerView;
    private CinemasAdapter adapter;
    private ArrayList<Cinema> cinemaItems = new ArrayList<>();
    private ArrayList<CinemaSettings> cinemaSettings = new ArrayList<>();
    private ProgressBar progressBar;

    public FragmentCinemas(){
        super(R.layout.fragment_cinemas);
        parse = new Parse();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cinemas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBarCinema);
        recyclerView = view.findViewById(R.id.recycler_view_cinemas);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        adapter = new CinemasAdapter(mainActivity, cinemaItems);
        recyclerView.setAdapter(adapter);

        Content content = new Content();
        content.execute();
    }


    /*private void init(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_in));

        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb(){
        try {
            Document doc = Jsoup.connect(BaseUrl).get();

            List<String> listName = null;
            Elements items = doc.select("div.cinemaList_name");
            for (Element item : items)
            {
                listName.add(item.text());
            }

            List<String> list = null;
            Elements itemsurl = doc.select("a.cinemaList_ref");
            for (Element item : itemsurl)
            {
                String value = item.attr("href");
                list.add(value);
            }

            for (int i = 0; i < list.size(); i++) {
                Document document = Jsoup.connect(list.get(i)).get();
                String listaddr = "";
                Elements itemsaddr = document.select("span.theaterInfo_dataAddr");
                for (Element item : itemsaddr) {
                    listaddr = item.text();
                }
                Cinema cinema = new Cinema(listName.get(i), listaddr);
                cinemaItems.add(cinema);
            }

            cinemaSettings.addAll(parse.CinemaWorker(doc));
            for(int i = 0; i < cinemaSettings.size(); i++) {
                Cinema cinema = new Cinema(cinemaSettings.get(i).getName(), cinemaSettings.get(i).getAddress());
                cinemaItems.add(cinema);
            }

            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_out));

            mainActivity.runOnUiThread(new Runnable() {
                public void run(){
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    @SuppressLint("StaticFieldLeak")
    public class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_in));
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(mainActivity, androidx.preference.R.anim.abc_fade_out));
            adapter.notifyDataSetChanged();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Document doc = Jsoup.connect(BaseUrl).get();
                Log.i("Logcat", "connect to webpage");
                Elements itemsUrl = doc.select("a.cinemaList_ref");
                ArrayList<String> listOfUrls = new ArrayList<>();
                for(Element url : itemsUrl) {
                    String value = url.attr("href"); // ссылки на кинотеатры
                    listOfUrls.add(value);
                    Log.i("Logcat", "Url: " + value);
                }

                Document docu = Jsoup.connect("https://msk.kinoafisha.info/cinema/map/").get();
                Elements itemsName = docu.select("div.cinemaList_name"); // имена кинотеатров
                Log.i("Logcat", "searched all names");

                Elements addr = docu.select("div.cinemaList_addr.as-mobile"); // адреса
                Log.i("Logcat", "searched all addresses");
                for (int i = 0; i < 100; i++)
                {
                    String key_id = String.valueOf(i);
                    Cinema cinema = new Cinema(itemsName.get(i).text(), addr.get(i).text(), listOfUrls.get(i), key_id, "0");
                    cinemaItems.add(cinema);
                }

                /*for (int i = 0; i < 10; i++) !!
                {
                    String value = itemsurl.get(i).attr("href");


                    Document document = Jsoup.connect(value).get();
                    Thread.sleep(700);
                    Log.i("Logcat", "connect to other webpage");

                    Element itemsaddr = document.selectFirst("span.theaterInfo_dataAddr");
                    String listaddr = "address here...";
                    if(itemsaddr != null)
                    {
                        listaddr = itemsaddr.data();
                    }
                    Log.i("Logcat", "Address: " + listaddr);

                    Cinema cinema = new Cinema(itemsName.get(i).text(), listaddr);
                    cinemaItems.add(cinema); !!
                }*/

                //Log.i("Logcat", "search all urls");
                /*for (int i = 0; i < listName.size(); i++) {
                    //Document document = Jsoup.connect(list.get(i)).get();
                    //String listaddr = "";
                    //Elements itemsaddr = document.select("span.theaterInfo_dataAddr");
                   // for (Element item : itemsaddr) {
                   //     listaddr = item.text();
                    //}
                    Cinema cinema = new Cinema(listName.get(i), "listaddr");
                    cinemaItems.add(cinema);
                }*/
                //Log.i("Logcat", "search all addresses");
                /*cinemaSettings.addAll(parse.CinemaWorker(doc));
                for(int i = 0; i < cinemaSettings.size(); i++){
                    Cinema cinema = new Cinema(cinemaSettings.get(i).getName(), cinemaSettings.get(i).getAddress());
                    cinemaItems.add(cinema);
                }*/
            }catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }
    }
}