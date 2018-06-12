package com.honeywell.audioassist.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.honeywell.audioassist.R;
import com.honeywell.audioassist.management.NotificationManagement;
import com.honeywell.audioassist.model.Event;
import com.honeywell.audioassist.repository.IEventRepository;
import com.honeywell.audioassist.repository.implementation.EventRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class EventListViewModel extends AndroidViewModel {

    private IEventRepository eventRepository;

    public EventListViewModel(@NonNull Application application) {
        super(application);
        initialize();
    }

    private void initialize(){
        eventRepository = new EventRepository();
        NotificationManagement.registerChannel(getApplication().getApplicationContext());
    }

    public FirestoreRecyclerOptions<Event> getAdapterOptions(LifecycleOwner owner){
        Query query = eventRepository.getEventListQuery();
        FirestoreRecyclerOptions.Builder<Event> builder = new FirestoreRecyclerOptions.Builder<>();
        return  builder.setQuery(query, new SnapshotParser<Event>() {
            @NonNull
            @Override
            public Event parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                return eventRepository.parseEvent(snapshot);
            }
        }).setLifecycleOwner(owner).build();
    }

    public void initializeMessaging(final Context context){
        FirebaseMessaging.getInstance().subscribeToTopic("alert").addOnCompleteListener(new OnCompleteListener<Void>(){

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context, R.string.notifications_error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getTodaysFailures(){

    }

}
