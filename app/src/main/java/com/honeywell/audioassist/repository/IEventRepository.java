package com.honeywell.audioassist.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.honeywell.audioassist.model.Event;

import java.util.Date;

public interface IEventRepository {

    Query getEventListQuery();
    Query getEventsFromDate(Date startDate);
    Event parseEvent(DocumentSnapshot doc);
}
