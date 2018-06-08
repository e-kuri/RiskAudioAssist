package com.honeywell.audioassist.repository.implementation;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.honeywell.audioassist.model.Event;
import com.honeywell.audioassist.repository.IEventRepository;

public class EventRepository implements IEventRepository{

    private FirebaseFirestore db;

    public Query getEventListQuery(){
        return getDb().collection("Events").orderBy(Event.Field.Time.getName(), Query.Direction.DESCENDING).limit(50);
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
}
