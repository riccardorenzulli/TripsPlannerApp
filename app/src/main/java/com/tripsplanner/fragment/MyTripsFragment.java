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
import com.tripsplanner.R;
import com.tripsplanner.entity.User;

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

    private RetainedFragment dataFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecentsTask recentsTask;
    private static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    protected static boolean permissions = false;
    private static boolean saveOffline;
    private int [] positions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytrips, container, false);
        if (savedInstanceState != null) positions = savedInstanceState.getIntArray("lastVisiblePosition");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.post_content);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("start");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        saveOffline = prefs.getBoolean("saveOffline",true);

        mLayoutManager = new StaggeredGridLayoutManager(this.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2,1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(positions != null) {
            mLayoutManager.scrollToPosition(positions[0]);
        }


        FragmentManager fm = ((Activity)this.getContext()).getFragmentManager();
        dataFragment = (RetainedFragment) fm.findFragmentByTag("RetainedFragment");

        if (dataFragment == null) {
            dataFragment = new RetainedFragment();
            fm.beginTransaction().add(dataFragment,"RetainedFragment").commit();
            fm.executePendingTransactions();
            checkStoragePermission();
            if(saveOffline && permissions) {
                //System.out.println("carico files locali");
                //Model.loadPosts();
            }

            else if (!saveOffline) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setMessage("Do you want to download your posts?");
                builder.setPositiveButton("Yes",this);
                builder.setNegativeButton("No",this);
                builder.show();
            }
        }

        //mAdapter = new RecentsAdapter(Model.getMyPosts(), this.getContext(), this.permissions, saveOffline);
        mRecyclerView.setAdapter(mAdapter);
        //Model.resetNewPost();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
                //ActivityCompat.requestPermissions(getActivity(),new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);

        }
        else permissions = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    this.permissions = true;
                    //Model.loadPosts();
                    //mAdapter = new RecentsAdapter(Model.getMyPosts(), this.getContext(), this.permissions, saveOffline);
                    mRecyclerView.setAdapter(mAdapter);
                    //Model.resetNewPost();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    this.permissions = false;

                }
                break;

            }

        }

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String userGson = prefs.getString("user","");
        Gson gson = new Gson();
        String userID = gson.fromJson(userGson, User.class).getGoogleID();
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                recentsTask = new RecentsTask(this.getContext(), permissions);
                recentsTask.execute(userID);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
        }
    }

    @Override
    public void onRefresh() {
/*        if(Model.getMyPosts().size()==0)
            onClick(new DialogInterface() {
                @Override
                public void cancel() {

                }

                @Override
                public void dismiss() {

                }
            }, DialogInterface.BUTTON_POSITIVE);
        else swipeRefreshLayout.setRefreshing(false);*/

    }

    protected class RecentsTask extends AsyncTask<String, Void, String> {
        private Context context;
        private boolean permissions;

        public RecentsTask(Context context, boolean permissions) {
            this.context = context;
            this.permissions = permissions;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... urls) {
            String user = urls[0];
            //DBHelperOnline.downloadPost(0,null,true,user);
            System.out.print("AsynkTask Started!");
            return "My post founded!";
        }

        @Override
        protected void onPostExecute(String result) {
/*            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Model.getContext());
            saveOffline = prefs.getBoolean("saveOffline",true);
            if (saveOffline) {
                ArrayList<Post> myPosts = Model.getMyPosts();
                for(int i = 0; i<myPosts.size(); i++)
                    Model.insertPostOffline(myPosts.get(i));
            }
            mAdapter = new RecentsAdapter(Model.getMyPosts(), this.context, this.permissions,saveOffline);
            mRecyclerView.setAdapter(mAdapter);
            swipeRefreshLayout.setRefreshing(false);*/
        }
    }

}