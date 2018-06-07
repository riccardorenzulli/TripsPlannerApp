package com.tripsplanner.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripsplanner.R;
import com.tripsplanner.entity.Place;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DayItineraryAdapter extends RecyclerView.Adapter<DayItineraryAdapter.PlaceViewHolder> {
    private Context context = null;
    private List<Place> places = null;

    public DayItineraryAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }


    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent,false);
        return new PlaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = this.places.get(position);
        holder.placeTextView.setText(place.getName());
        holder.descriptionTextView.setText(place.getDescription());
        new BitmapDownloaderTask(holder.placeImageView).execute(place.getPhotosUrl());

    }

    @Override
    public int getItemCount() {
        return this.places.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView placeImageView;
        TextView placeTextView;
        TextView descriptionTextView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            placeImageView = (ImageView) itemView.findViewById(R.id.placeImageView);
            placeTextView = (TextView) itemView.findViewById(R.id.placeTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.placeDescription);
        }

        @Override
        public void onClick(View v) {

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
