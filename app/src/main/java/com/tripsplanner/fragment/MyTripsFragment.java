package com.tripsplanner.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tripsplanner.R;
import com.tripsplanner.adapter.HomeTripAdapter;
import com.tripsplanner.entity.BasicTrip;
import com.tripsplanner.entity.Trip;
import com.tripsplanner.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.widget.SwipeRefreshLayout.*;

/**
 * Authors: Riccardo Renzulli, Gabriele Sartor<br>
 * Università degli Studi di Torino<br>
 * Department of Computer Science<br>
 * Date: June 2016<br><br>
 * <p/>
 * riccardo.renzulli@edu.unito.it<br>
 * gabriele.sartor@edu.unito.it<br><br>
 */

public class MyTripsFragment extends Fragment implements DialogInterface.OnClickListener, OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyTripsTask myTripsTask;
    private List<BasicTrip> myTrips = new ArrayList<BasicTrip>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytrips, container, false);
        //if (savedInstanceState != null) positions = savedInstanceState.getIntArray("lastVisiblePosition");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.trips_content);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("start");
        mLayoutManager = new StaggeredGridLayoutManager(this.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2,1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new HomeTripAdapter(this.getContext(), myTrips);
        mRecyclerView.setAdapter(mAdapter);
        myTripsTask = new MyTripsTask(this.getContext());
        myTripsTask.execute();


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putIntArray("lastVisiblePosition",mLayoutManager.findFirstVisibleItemPositions(null));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                myTripsTask = new MyTripsTask(this.getContext());
                myTripsTask.execute();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    public void onRefresh() {
        if(this.myTrips.size()==0)
            onClick(new DialogInterface() {
                @Override
                public void cancel() {

                }

                @Override
                public void dismiss() {

                }
            }, DialogInterface.BUTTON_POSITIVE);
        else swipeRefreshLayout.setRefreshing(false);

    }

    protected class MyTripsTask extends AsyncTask<String, Void, String> {
        private Context context;

        public MyTripsTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.print("AsynkTask onPreExecute!");
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            System.out.print("AsynkTask Started!");
            try {
                getMyTrips(getMyTripsQuery());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "My post founded!";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.print("AsynkTask onPostExecute!");
            mAdapter = new HomeTripAdapter(this.context, myTrips);
            mRecyclerView.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }

        private String getMyTripsQuery() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            String userGson = prefs.getString("user","");
            Gson gson = new Gson();
            long userID = gson.fromJson(userGson, User.class).getId();
            System.out.println("UserID:"+userID);

            String url = "http://ec2-18-130-53-112.eu-west-2.compute.amazonaws.com:8080/TripsPlanner-war/webresources/mybasictrips?id="+userID;
            String query = new StringBuilder(url).toString();

            return query;
        }

        private void getMyTrips(String query) throws MalformedURLException, IOException {
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

            myTrips = gson.fromJson(jsonResult, new TypeToken<List<BasicTrip>>(){}.getType());
        }
    }

}