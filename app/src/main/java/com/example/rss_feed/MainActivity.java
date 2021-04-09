package com.example.rss_feed;
// Name: Sharon Shaji
// Student ID: S1623993

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    ListView lvdata;
    TextView view, violent, vstrong, moderate, weak;
    ArrayList<RssItem> parsedData;
    ArrayList<String> sortedMag;
    ArrayList<RssItem> list;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    RssListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parsedData = new ArrayList<RssItem>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");

        // Raw links to the graphical components
        lvdata = (ListView) findViewById(R.id.lvdata);
        view = (TextView) findViewById(R.id.view);
        violent = (TextView) findViewById(R.id.violent);
        vstrong = (TextView) findViewById(R.id.vstrong);
        moderate = (TextView) findViewById(R.id.moderate);
        Log.e("MyTag", "after startButton");


        //GET MORE DETAILED INFO ON THE SELECTED EARTHQUAKE
        lvdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                RssItem item = (RssItem) parent.getItemAtPosition(position);
                Uri uri = Uri.parse(item.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        new ProcessInBackground().execute();

        //Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_search:
                        Intent intent = new Intent(getApplicationContext(), Search.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("EQuakeSearch", parsedData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_home:
                        return true;

                    case R.id.navigation_map:
                        Intent intent1 = new Intent(getApplicationContext(), Map.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("EQuake", parsedData);
                        intent1.putExtras(bundle1);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_exit:
                        finish();
                        System.exit(0);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public static InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public void sort(View view) {

        Collections.sort(list, RssItem.magDesc);
        adapter.sort(RssItem.magDesc);
        adapter = new RssListAdapter(MainActivity.this, R.layout.list_view, list);
        lvdata.setAdapter(adapter);
    }


    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {

        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Exception doInBackground(Integer... params) {

            try {
                URL feed_url = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(getInputStream(feed_url), "UTF_8");
                boolean inItem = false;

                int eventType = parser.getEventType();
                RssItem item = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = null;

                    if (eventType == XmlPullParser.START_TAG) {
                        name = parser.getName();

                        if (name.equalsIgnoreCase("item")) {
                            inItem = true;
                            item = new RssItem();

                        } else if (name.equalsIgnoreCase("title")) {
                            if (inItem) {
                                String temp = parser.nextText();
                                Log.i("MyTag", "IN TITLE - " + temp);
                                item.setTitle(temp);
                            }

                        } else if (name.equalsIgnoreCase("description")) {
                            if (inItem) {
                                String temp = parser.nextText();
                                Log.i("MyTag", "IN DESCRIPTION - " + temp);
                                item.setDescription(temp);

                                String[] desElement;
                                desElement = temp.split("([A-Za-z\\/ ]+?):");

                                // GET DEPTH FROM DESCRIPTION TAG
                                item.setDepth(Integer.parseInt(desElement[4].replace(" km ;", "").trim()));
                                // GET Magnitude FROM DESCRIPTION TAG
                                item.setMagnitude(Double.parseDouble(desElement[5].trim()));
                            }

                        } else if (name.equalsIgnoreCase("link")) {
                            if (inItem) {
                                String temp = parser.nextText();
                                Log.i("MyTag", "IN LINK - " + temp);
                                item.setLink(temp);
                            }

                        } else if (name.equalsIgnoreCase("pubDate")) {
                            if (inItem) {
                                String temp = parser.nextText();
                                Log.i("MyTag", "IN PUBLICATION DATE - " + temp);
                                item.setPubDate(temp);
                            }

                        } else if (name.equalsIgnoreCase("geo:lat")) {
                            if (inItem) {
                                String temp = parser.nextText();
                                Log.i("MyTag", "IN LATITUDE - " + temp);
                                item.setLatitude(Double.valueOf(temp));
                            }

                        } else if (name.equalsIgnoreCase("geo:long")) {
                            if (inItem) {
                                String temp = parser.nextText();
                                Log.i("MyTag", "IN LONGITUDE - " + temp);
                                item.setLongitude(Double.valueOf(temp));
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                        Log.i("My tag", "Found End Tag");
                        inItem = false;
                        parsedData.add(item);
                    }

                    eventType = parser.next();
                }
            } catch (MalformedURLException | XmlPullParserException e) {
                exception = e;
            } catch (IOException e) {
                exception = e;
            }
            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            List<RssItem> list = new ArrayList<>(parsedData);

            adapter = new RssListAdapter(MainActivity.this, R.layout.list_view, (ArrayList<RssItem>) list);
            lvdata.setAdapter(adapter);
        }
    }
}// End of Main Activity