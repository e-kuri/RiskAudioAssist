package com.honeywell.audioassist.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honeywell.audioassist.R;
import com.honeywell.audioassist.viewmodel.EventListViewModel;

public class EventHistoryFragment extends Fragment {

    private RecyclerView mEventsContainer;
    private EventListRecyclerAdapter mAdapter;
    private EventListViewModel mViewModel;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(EventListViewModel.class);
        mEventsContainer = view.findViewById(R.id.events_container);
        mEventsContainer.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mEventsContainer.setLayoutManager(mLayoutManager);
        mAdapter = new EventListRecyclerAdapter(mViewModel.getAdapterOptions(this));
        mEventsContainer.setAdapter(mAdapter);

        mViewModel.initializeMessaging(this.getContext());
        return view;
    }
}
