package com.tripsplanner.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tripsplanner.R;
import com.tripsplanner.adapter.DayItineraryAdapter;
import com.tripsplanner.adapter.HomeTripAdapter;
import com.tripsplanner.entity.BasicTrip;
import com.tripsplanner.entity.Place;
import com.tripsplanner.fragment.MyTripsFragment;

import java.util.ArrayList;
import java.util.List;

public class DayItineraryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayout linearLayout;
    private StaggeredGridLayoutManager mLayoutManager;
    private List<Place> places = new ArrayList<Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_itinerary);
        Intent intent = getIntent();
        String dayPlaces = intent.getStringExtra("dayPlaces");
        Gson gson = new Gson();
        places = gson.fromJson(dayPlaces, new TypeToken<List<Place>>(){}.getType());

        linearLayout = (LinearLayout) findViewById(R.id.places_content);
        mRecyclerView = (RecyclerView) linearLayout.findViewById(R.id.recycler_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("start");
        mLayoutManager = new StaggeredGridLayoutManager(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2,1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new DayItineraryAdapter(this, places);
        mRecyclerView.setAdapter(mAdapter);
        

    }



}
