package com.honeywell.audioassist.view;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.honeywell.audioassist.R;
import com.honeywell.audioassist.viewmodel.EventListViewModel;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView mEventsContainer;
    private EventListRecyclerAdapter mAdapter;
    private EventListViewModel mViewModel;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
        mEventsContainer = findViewById(R.id.events_container);
        mEventsContainer.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mEventsContainer.setLayoutManager(mLayoutManager);
        mAdapter = new EventListRecyclerAdapter(mViewModel.getAdapterOptions(this));
        mEventsContainer.setAdapter(mAdapter);

        mViewModel.initializeMessaging(getApplicationContext());
    }
}
