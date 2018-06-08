package com.honeywell.audioassist.view;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.honeywell.audioassist.R;
import com.honeywell.audioassist.model.Event;

import java.text.SimpleDateFormat;

public class EventListRecyclerAdapter extends FirestoreRecyclerAdapter<Event, EventListRecyclerAdapter.ViewHolder> {

    private FirestoreRecyclerOptions<Event> options;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EventListRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Event> options) {
        super(options);
        this.options = options;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Event model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView eventTitle;
        private TextView location;
        private TextView date;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.icon);
            this.location = itemView.findViewById(R.id.location);
            this.date = itemView.findViewById(R.id.date);
            this.eventTitle = itemView.findViewById(R.id.event_title);
        }

        public void bind(Event event){
            this.eventTitle.setText(event.getType());
            this.location.setText(event.getLocation());
            Drawable icon;
            switch (event.getSeverity()){
                case 1:
                    icon = this.image.getResources().getDrawable(R.drawable.ic_danger);
                    break;
                default:
                    icon = this.image.getResources().getDrawable(R.drawable.ic_warning);
                    break;
            }
            Glide.with(this.image.getContext()).load(icon).into(this.image);
            if(event.getTime() != null){
                this.date.setText(dateFormat.format(event.getTime()));
            }else {
                this.date.setText("");
            }
        }
    }
}
