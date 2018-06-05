package com.tripsplanner.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripsplanner.R;
import com.tripsplanner.entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class HomeTripAdapter extends RecyclerView.Adapter<HomeTripAdapter.TripViewHolder> {
    private Context context = null;
    private List<Trip> myTrips = null;

    public HomeTripAdapter(Context context, List<Trip> trips) {
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
        Trip trip = this.myTrips.get(position);
        holder.tripImageView.setImageURI(Uri.parse(trip.getDayPlaces(0).get(1).getPhotosUrl()));
        holder.tripTextView.setText(trip.getSearch().getDestinationCity());
    }

    @Override
    public int getItemCount() {
        return this.myTrips.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView tripImageView;
        TextView tripTextView;

        public TripViewHolder(View itemView) {
            super(itemView);
            tripImageView = (ImageView) itemView.findViewById(R.id.tripImageView);
            //tripImageView.setOnLongClickListener(this);
            tripImageView.setOnClickListener(this);
            tripTextView = (TextView) itemView.findViewById(R.id.tripTextView);
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
            /*Intent intent = new Intent(v.getContext(), DisplayPostActivity.class);
            String id = myPosts.get(getPosition()).getID();
            intent.putExtra("ID",id);
            v.getContext().startActivity(intent);*/
        }
    }

}
