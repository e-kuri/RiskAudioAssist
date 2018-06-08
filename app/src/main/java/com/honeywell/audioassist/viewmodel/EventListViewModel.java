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
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");

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
                return parseEvent(snapshot);
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

    private Event parseEvent(DocumentSnapshot doc){
        Event event = new Event();
        Map<String, Object> snapshot = doc.getData();
        Object time = snapshot.get(Event.Field.Time.getName());
        if(time instanceof Long){
            event.setTime(new Date((Long)time));
        }else if(time instanceof Date){
            event.setTime((Date) time);
        }else if(time instanceof String){
            try {
                event.setTime(dateFormat.parse((String) time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Object severity = snapshot.get(Event.Field.Severity.getName());
        if( severity != null){
            if(severity instanceof Integer){
                event.setSeverity((Integer) severity);
            }else if(severity instanceof String){
                try{
                    event.setSeverity(Integer.parseInt((String) severity));
                }catch (NumberFormatException ex){
                    event.setSeverity(2);
                }
            }else if(severity instanceof Long){
                event.setSeverity( ((Long)severity).intValue() );
            }
        }else{
            event.setSeverity(2);
        }

        Object location = snapshot.get(Event.Field.Location.getName());
        if(location != null){
            event.setLocation(location.toString());
        }

        Object type = snapshot.get(Event.Field.Type.getName());
        if(type != null){
            event.setType(type.toString());
        }

        return event;
    }

}
