package com.honeywell.audioassist.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.honeywell.audioassist.model.Event;
import com.honeywell.audioassist.repository.IEventRepository;
import com.honeywell.audioassist.repository.implementation.EventRepository;
import com.google.firebase.firestore.EventListener;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class StatisticsViewModel extends ViewModel{

    private IEventRepository eventRepository;
    private ListenerRegistration mWeekRegistration;
    private ListenerRegistration mTodayRegistration;
    private MutableLiveData<Map<Integer, Integer> > mWeeklyEvents;
    private MutableLiveData<Integer> mTodaysEvents;
    private static final int WEEKS_TO_QUERY = 4;
    private static final String TAG = StatisticsViewModel.class.getName();
    private final Date firstDate;
    private static final long MILLISECONDS_IN_DAY = (long) (8.64*Math.pow(10, 7));

    public StatisticsViewModel(){
        eventRepository = new EventRepository();
        mWeeklyEvents = new MutableLiveData<>();
        firstDate = getFirstDayToQuery();
        mTodaysEvents = new MutableLiveData<>();
    }

    public MutableLiveData<Map<Integer, Integer>> getmWeeklyEvents() {
        return mWeeklyEvents;
    }

    public MutableLiveData<Integer> getmTodaysEvents() {
        return mTodaysEvents;
    }

    public void stopListening(){
        mWeekRegistration.remove();
        mTodayRegistration.remove();
    }

    public void startListening(){
        mWeekRegistration = eventRepository.getEventsFromDate(firstDate).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                mWeeklyEvents.postValue(parseQuerySnapshotToWeek(queryDocumentSnapshots));
            }
        });

        mTodayRegistration = eventRepository.getEventsFromDate(getBeginingOfDay(new Date())).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                mTodaysEvents.postValue(sumEvents(queryDocumentSnapshots));
            }
        });
    }

    private int sumEvents(QuerySnapshot snapshot){
        int sum = 0;
        for(QueryDocumentSnapshot doc : snapshot){
            sum++;
        }
        return sum;
    }

    private Date getBeginingOfDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Map<Integer, Integer> parseQuerySnapshotToWeek(QuerySnapshot snapshot){
        Map<Integer, Integer> resultMap = new HashMap<>();
        int weeksAgo = WEEKS_TO_QUERY;
        long firstDayMillis =  firstDate.getTime();
        int sum = 0;
        Log.d(TAG, "week millis: " +  7 * MILLISECONDS_IN_DAY );
        for(QueryDocumentSnapshot doc : snapshot){
            Event event = eventRepository.parseEvent(doc);
            Log.d(TAG, "firstDayMillis: " + firstDayMillis + ", event time: " + event.getTime().getTime());
            while( event.getTime().getTime() - firstDayMillis > 7*MILLISECONDS_IN_DAY && weeksAgo > 0){
                resultMap.put(weeksAgo, sum);
                sum = 0;
                weeksAgo--;
                firstDayMillis += 7 * MILLISECONDS_IN_DAY;
            }
            sum++;
        }
        resultMap.put(weeksAgo, sum);
        return resultMap;
    }

    private Date getFirstDayToQuery(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -7 * WEEKS_TO_QUERY);
        calendar.add(Calendar.DAY_OF_YEAR, Calendar.SUNDAY - calendar.get(Calendar.DAY_OF_WEEK) );
        return calendar.getTime();
    }

}
