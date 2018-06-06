package com.tripsplanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripsplanner.R;
import com.tripsplanner.activity.MainActivity;
import com.tripsplanner.entity.BasicTrip;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeTripAdapter extends RecyclerView.Adapter<HomeTripAdapter.TripViewHolder> {
    private Context context = null;
    private List<BasicTrip> myTrips = null;

    public HomeTripAdapter(Context context, List<BasicTrip> trips) {
        this.context = context;
        this.myTrips = trips;
    }


    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item,parent,false);
        return new TripViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, int position) {
        BasicTrip trip = this.myTrips.get(position);
        String tripCityAndDate = trip.getDestination() + "\n" +
                trip.getDepartureDate().toString();
        //holder.tripImageView.setImageURI(Uri.parse(trip.getDayPlaces(0).get(1).getPhotosUrl()));
        holder.tripTextView.setText(tripCityAndDate);
        //holder.tripDateTextView.setText(tripDate);
        new BitmapDownloaderTask(holder.tripImageView).execute(trip.getPlaceIMGUrl());

    }

    @Override
    public int getItemCount() {
        return this.myTrips.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tripImageView;
        TextView tripTextView;
        TextView tripDateTextView;

        public TripViewHolder(View itemView) {
            super(itemView);
            tripImageView = (ImageView) itemView.findViewById(R.id.tripImageView);
            //tripImageView.setOnLongClickListener(this);
            tripImageView.setOnClickListener(this);
            tripTextView = (TextView) itemView.findViewById(R.id.tripTextView);
            tripDateTextView = (TextView) itemView.findViewById(R.id.date_trip);
        }

/*        @Override
        public boolean onLongClick(View v) {
            if(dialog==null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Vuoi eliminare il post?");
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Model.deletePostById(myPosts.get(getPosition()).getID());
                        int position = getPosition();
                        removeItem(position);
                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                dialog = builder.create();
            }
            dialog.show();
            dialog = null;
            return true;
        }*/

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            long id = myTrips.get(getPosition()).getIdTrip();
            intent.putExtra("ID",id);
            v.getContext().startActivity(intent);
        }

    }

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            return getImageBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        private Bitmap getImageBitmap(String url) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Error getting bitmap", e);
            }
            return bm;
        }
    }

}
