package com.tripsplanner.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tripsplanner.R;
import com.tripsplanner.activity.DayItineraryActivity;
import com.tripsplanner.activity.MainActivity;
import com.tripsplanner.entity.BasicTrip;
import com.tripsplanner.entity.DayItinerary;
import com.tripsplanner.entity.Place;
import com.tripsplanner.entity.Route;
import com.tripsplanner.entity.Trip;
import com.tripsplanner.entity.User;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayViewHolder> {

    private Context context = null;
    private List<DayItinerary> dayItineraries = null;
    private Trip myTrip = null;

    public DaysAdapter(Context context, List<DayItinerary> dayItineraries, Trip myTrip) {
        this.context = context;
        this.dayItineraries = dayItineraries;
        this.myTrip = myTrip;
    }

    @NonNull
    @Override
    public DaysAdapter.DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item,parent,false);
        return new DaysAdapter.DayViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysAdapter.DayViewHolder holder, int position) {
        DayItinerary itinerary = this.dayItineraries.get(position);
        String imageURL = itinerary.getLegs().get(0).getArrivalPlace().getPhotosUrl();

        String dayNumber = "Day "+(position+1);
        holder.dayTextView.setText(dayNumber);
        new DaysAdapter.BitmapDownloaderTask(holder.dayImageView).execute(imageURL);
    }

    @Override
    public int getItemCount() {
        return dayItineraries.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView dayImageView;
        TextView dayTextView;
        TextView dayDescriptionTextView;

        public DayViewHolder(View itemView) {
            super(itemView);
            dayImageView = (ImageView) itemView.findViewById(R.id.dayImageView);
            dayImageView.setOnClickListener(this);
            dayTextView = (TextView) itemView.findViewById(R.id.dayTextView);
            dayDescriptionTextView = (TextView) itemView.findViewById(R.id.dayDescription);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), DayItineraryActivity.class);
            List<Place> places = myTrip.getDayPlaces(getPosition());
            List<Route> routes = myTrip.getDayRoute(getPosition());
            Gson gson = new Gson();
            String placesJson = gson.toJson(places);
            String routesJson = gson.toJson(routes);
            intent.putExtra("dayPlaces",placesJson);
            intent.putExtra("dayRoutes",routesJson);
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
