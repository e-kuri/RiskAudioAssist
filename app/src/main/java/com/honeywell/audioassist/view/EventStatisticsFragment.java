package com.honeywell.audioassist.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honeywell.audioassist.R;
import com.honeywell.audioassist.viewmodel.StatisticsViewModel;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Map;

public class EventStatisticsFragment extends Fragment {

    private GraphView barChart;
    private StatisticsViewModel mViewModel;
    private TextView mTodayEventsText;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StatisticsViewModel.class);
        mTodayEventsText = view.findViewById(R.id.today_value);

        final Observer<Map<Integer,Integer>> eventObserver = new Observer<Map<Integer, Integer>>() {
            @Override
            public void onChanged(@Nullable final Map<Integer, Integer> weeklyEvents) {
                // Update the UI, in this case, a TextView.
                setChartValues(weeklyEvents);
            }
        };

        final Observer<Integer> todayEventsObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                mTodayEventsText.setText(value.toString());
            }
        };

        mViewModel.getmTodaysEvents().observe(this, todayEventsObserver);
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mViewModel.getmWeeklyEvents().observe(this, eventObserver);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.stopListening();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container, false);
        barChart = view.findViewById(R.id.graph);

        barChart.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (!isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " w ago";
                }
            }
        });

        return view;
    }

    private void setChartValues(Map<Integer, Integer> weeklyEvents){
           DataPoint[] points = new DataPoint[weeklyEvents.size()];
           for(Integer key : weeklyEvents.keySet()){
                points[key] = new DataPoint(key, weeklyEvents.get(key));
           }
           barChart.removeAllSeries();
           BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        series.setSpacing(5);
        series.setDrawValuesOnTop(true);
        barChart.addSeries(series);
    }
}
