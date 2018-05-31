package com.tripsplanner.fragment;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;

/**
 * Authors: Riccardo Renzulli, Gabriele Sartor<br>
 * Università degli Studi di Torino<br>
 * Department of Computer Science<br>
 * Date: June 2016<br><br>
 * <p/>
 * riccardo.renzulli@edu.unito.it<br>
 * gabriele.sartor@edu.unito.it<br><br>
 */

public class RetainedFragment extends Fragment {

    private static final String TAG = "RetainedFragment";
    private LruCache<String, Bitmap> data;

    public RetainedFragment() {
        data = null;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    public Object getData() {
        return data;
    }

    public void setData(LruCache<String, Bitmap> data) {
        this.data = data;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Fragment retained distrutto");
    }

}
