package com.tripsplanner.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.tripsplanner.R;
import com.tripsplanner.adapter.PagerAdapter;
import com.tripsplanner.entity.User;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable iconSettings = ContextCompat.getDrawable(getApplicationContext(),R.drawable.settings_selector);
        if (toolbar != null) {
            toolbar.setOverflowIcon(iconSettings);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.inflateMenu(R.menu.menu_main);
        }
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText("MY TRIPS"));
            tabLayout.addTab(tabLayout.newTab().setText("SHARED TRIPS"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userGson = preferences.getString("user","");
        ImageView profile = (ImageView) findViewById(R.id.profile);
        Gson gson = new Gson();
        //profile.setImageDrawable(getResources().getDrawable(R.drawable.profile_default));
        String picturePath = gson.fromJson(userGson,User.class).getImgURL();
        System.out.println(picturePath);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL newurl;

        try {
            newurl = new URL(picturePath);
            Bitmap bitmapProfile = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),bitmapProfile);
            dr.setCornerRadius(Math.max(bitmapProfile.getWidth(), bitmapProfile.getHeight()) / 2.0f);
            profile.setImageDrawable(dr);
        } catch (Exception e) {
            profile.setImageDrawable(getResources().getDrawable(R.drawable.profile_default));
        }

    }

    public void startProfileActivity(View view) {
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
