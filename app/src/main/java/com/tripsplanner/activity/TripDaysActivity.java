package com.tripsplanner.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tripsplanner.R;
import com.tripsplanner.adapter.DaysAdapter;
import com.tripsplanner.adapter.HomeTripAdapter;
import com.tripsplanner.entity.BasicTrip;
import com.tripsplanner.entity.DayItinerary;
import com.tripsplanner.entity.Trip;
import com.tripsplanner.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class TripDaysActivity extends AppCompatActivity {
    private MyTripTask myTripTask;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private LinearLayout linearLayout;
    private long idUser;
    private long idTrip;
    private Trip myTrip;
    private List<DayItinerary> itineraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_days);

        linearLayout = (LinearLayout) findViewById(R.id.days_content);
        mRecyclerView = (RecyclerView) linearLayout.findViewById(R.id.recycler_view);

        Intent intent = getIntent();
        idUser = intent.getLongExtra("id_user", 0);
        idTrip = intent.getLongExtra("id_trip", 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("start");
        mLayoutManager = new StaggeredGridLayoutManager(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2,1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
/*        mAdapter = new DaysAdapter(this, itineraries);
        mRecyclerView.setAdapter(mAdapter);*/
        myTripTask = new MyTripTask(this);
        myTripTask.execute();



    }
    protected class MyTripTask extends AsyncTask<String, Void, String> {
        private Context context;

        public MyTripTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.print("AsynkTask onPreExecute!");
        }

        @Override
        protected String doInBackground(String... urls) {
            System.out.print("AsynkTask Started!");
            try {
                getMyTrip(getMyTripQuery());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "My post founded!";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.print("AsynkTask onPostExecute!");
            mAdapter = new DaysAdapter(this.context, itineraries, myTrip);
            mRecyclerView.setAdapter(mAdapter);
        }

        private String getMyTripQuery() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            String userGson = prefs.getString("user","");
            Gson gson = new Gson();
            long userID = gson.fromJson(userGson, User.class).getId();
            System.out.println("UserID:"+userID);

            String url = "http://ec2-18-130-53-112.eu-west-2.compute.amazonaws.com:8080/TripsPlanner-war/webresources/trip?userID="+idUser+"&id="+idTrip;
            String query = new StringBuilder(url).toString();

            return query;
        }

        private void getMyTrip(String query) throws MalformedURLException, IOException {
            System.out.println("Query:"+query);
            URL obj = new URL(query);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line = read.readLine();
            StringBuilder sb = new StringBuilder();

            while(line!=null) {
                System.out.println(line);
                sb.append(line);
                line = read.readLine();
            }

            String jsonResult = sb.toString();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            myTrip = gson.fromJson(jsonResult, Trip.class);
            itineraries = myTrip.getItineraries();
        }
    }
}
