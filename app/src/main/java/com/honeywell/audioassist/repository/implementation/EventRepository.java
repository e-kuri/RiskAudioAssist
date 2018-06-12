package com.honeywell.audioassist.repository.implementation;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.honeywell.audioassist.model.Event;
import com.honeywell.audioassist.repository.IEventRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class EventRepository implements IEventRepository{

    private FirebaseFirestore db;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss");

    public Query getEventListQuery(){
        return getDb().collection(Collection.Event.getName()).orderBy(Event.Field.Time.getName(), Query.Direction.DESCENDING).limit(50);
    }

    public Query getEventsFromDate(Date startDate){
        return getDb().collection(Collection.Event.getName()).whereGreaterThanOrEqualTo(Event.Field.Time.getName(), startDate.getTime())
                .orderBy(Event.Field.Time.getName(), Query.Direction.ASCENDING);
    }

    private void initialize(){
        this.db = FirebaseFirestore.getInstance();
    }

    private FirebaseFirestore getDb(){
        if(db == null){
            initialize();
        }
        return db;
    }

    public Event parseEvent(DocumentSnapshot doc){
        Event event = new Event();
        Map<String, Object> snapshot = doc.getData();
        Object time = snapshot.get(Event.Field.Time.getName());
        if(time instanceof Long){
            event.setTime(new Date((Long)time));
        }else if(time instanceof Date){
            event.setTime((Date) time);
        }else if(time instanceof String){
            try {
                event.setTime(new Date(Long.parseLong((String) time)));
            } catch (NumberFormatException e) {
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
