package com.example.rss_feed;
// Name: Sharon Shaji
// Student ID: S1623993

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Search extends AppCompatActivity {
    Date startDate, endDate;
    EditText etmindate, etmaxdate;
    TextView txtnorthdata, txtsouthdata, txtwestdata, txteastdata, txtdeepdata, txtmagdata, txtshallowdata;
    ImageView calendar, calendar2;
    Button btnsearch;
    private int mDate, mMonth, mYear;
    private final long millisecondsInDay = 86400000;
    private ArrayList<RssItem> earthquakeData;
    private ArrayList<RssItem> equakeFilteredData;
    private RssItem mMag, mDepth, mShallow, mNortherly, mSoutherly, mWesterly, mEasterly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        equakeFilteredData = new ArrayList<>();

        earthquakeData = (ArrayList<RssItem>) getIntent().getExtras().getSerializable("EQuakeSearch");
        Log.i("MyTag", "EQuakeSearch Size - " + earthquakeData.size());

        etmindate = findViewById(R.id.etmindate);
        calendar = findViewById(R.id.calendar);
        etmaxdate = findViewById(R.id.etmaxdate);
        calendar2 = findViewById(R.id.calendar2);
        btnsearch = findViewById(R.id.btnsearch);
        txtnorthdata = findViewById(R.id.txtnorthdata);
        txtsouthdata = findViewById(R.id.txtsouthdata);
        txteastdata = findViewById(R.id.txteastdata);
        txtwestdata = findViewById(R.id.txtwestdata);
        txtdeepdata = findViewById(R.id.txtdeepdata);
        txtshallowdata = findViewById(R.id.txtshallowdata);
        txtmagdata = findViewById(R.id.txtmagdata);

        //Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.navigation_search);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_search:
                        return true;

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_map:
                        Intent intent1 = new Intent(getApplicationContext(), Map.class);
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("EQuake", earthquakeData);
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

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //FILTERS THE LIST
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                for (RssItem item : earthquakeData) {
                    try {
                        Date date = dateFormat2.parse(item.getPubDate());
                        if (date.after(startDate) && date.before(endDate)) {
                            equakeFilteredData.add(item);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (equakeFilteredData.size() > 0) {
                    mNortherly = mostNortherly(equakeFilteredData);
                    mSoutherly = mostSoutherly(equakeFilteredData);
                    mWesterly = mostWesterly(equakeFilteredData);
                    mEasterly = mostEasterly(equakeFilteredData);
                    mDepth = highestDepth(equakeFilteredData);
                    mShallow = lowestDepth(equakeFilteredData);
                    mMag = largestMagnitude(equakeFilteredData);
                }
            }
        });
    }

    //SEARCHES THE DATA USING THE FILTERED LIST
                    /*northely = highest latitude
                        south = lowest latitude
                        east = highest long
                        west = lowest long*/

    //MOST NORTHERLY
    public RssItem mostNortherly(ArrayList<RssItem> equakeFilteredData) {
        double nlat = -Double.MAX_VALUE;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getLatitude() > nlat) {
                nlat = equakeFilteredData.get(i).getLatitude();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txtnorthdata.setText(equakeData.getLocation());
        return equakeData;
    }

    //MOST SOUTHERLY

    public RssItem mostSoutherly(ArrayList<RssItem> equakeFilteredData) {
        double slat = Double.MAX_VALUE;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getLatitude() < slat) {
                slat = equakeFilteredData.get(i).getLatitude();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txtsouthdata.setText(equakeData.getLocation());
        return equakeData;
    }

    //MOST EASTERLY

    public RssItem mostEasterly(ArrayList<RssItem> equakeFilteredData) {
        double elongitude = -Double.MAX_VALUE;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getLongitude() > elongitude) {
                elongitude = equakeFilteredData.get(i).getLatitude();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txteastdata.setText(equakeData.getLocation());
        return equakeData;
    }

    //MOST WESTERLY

    public RssItem mostWesterly(ArrayList<RssItem> equakeFilteredData) {
        double wlongitude = Double.MAX_VALUE;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getLongitude() < wlongitude) {
                wlongitude = equakeFilteredData.get(i).getLatitude();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txtwestdata.setText(equakeData.getLocation());
        return equakeData;
    }

    //MOST DEEPEST

    public RssItem highestDepth(ArrayList<RssItem> equakeFilteredData) {
        int hdepth = -Integer.MAX_VALUE;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getDepth() > hdepth) {
                hdepth = equakeFilteredData.get(i).getDepth();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txtdeepdata.setText(equakeData.getDepth() + " km, " + equakeData.getLocation());
        return equakeData;
    }

    //MOST SHALLOWEST

    public RssItem lowestDepth(ArrayList<RssItem> equakeFilteredData) {
        int ldepth = Integer.MAX_VALUE;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getDepth() < ldepth) {
                ldepth = equakeFilteredData.get(i).getDepth();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txtshallowdata.setText(equakeData.getDepth() + " km, " + equakeData.getLocation());
        return equakeData;
    }

    //LARGEST MAGNITUDE

    public RssItem largestMagnitude(ArrayList<RssItem> equakeFilteredData) {
        double mag = 0;
        RssItem equakeData = null;

        for (int i = 0; i < equakeFilteredData.size(); i++) {
            if (equakeFilteredData.get(i).getMagnitude() > mag) {
                mag = equakeFilteredData.get(i).getMagnitude();
                equakeData = equakeFilteredData.get(i);
            }
        }
        txtmagdata.setText(equakeData.getMagnitude() + ", " + equakeData.getLocation());
        return equakeData;
    }

    // START DATE PICKER

    public void startDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        mDate = calendar.get(Calendar.DATE);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datepicker = new DatePickerDialog(com.example.rss_feed.Search.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                month += 1;
                String filteredDate = date + "/" + month + "/" + year;
                etmindate.setText(filteredDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    startDate = dateFormat.parse(filteredDate);
                    Log.i("MyTag", "Formatting startDate");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDate);
        datepicker.show();
    }

    // END DATE PICKER

    public void endDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        mDate = calendar.get(Calendar.DATE);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datepicker1 = new DatePickerDialog(com.example.rss_feed.Search.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {
                month += 1;
                String filteredDate1 = date + "/" + month + "/" + year;
                etmaxdate.setText(filteredDate1);
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    endDate = dateFormat1.parse(filteredDate1);
                    Log.i("MyTag", "Formatting endDate");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDate);
        datepicker1.show();
    }
}// End of Main Activity