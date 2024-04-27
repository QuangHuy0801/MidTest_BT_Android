package com.example.midtest.Ativity;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.midtest.Adapter.StatisticAdapter;
import com.example.midtest.Model.Statistic;
import com.example.midtest.R;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {

    ListView listView;
    View view;
    ArrayList<Statistic> data = new ArrayList<>();
    StatisticAdapter statisticAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        setControl();
        setEvent();
        return view;
    }

    private void setEvent() {
    }

    private void setControl() {
        Context context = getContext();
        listView = view.findViewById(R.id.lvStatistic);
        data.add(new Statistic(1, context.getString(R.string.title_doanh_thu), context.getString(R.string.text_doanh_thu)));
        data.add(new Statistic(2, context.getString(R.string.title_san_pham_ban_ra), context.getString(R.string.text_san_pham_ban_ra)));
        data.add(new Statistic(3, context.getString(R.string.title_san_pham),  context.getString(R.string.text_san_pham)));
        data.add(new Statistic(4, context.getString(R.string.title_don_vi_san_pham), context.getString(R.string.text_don_vi_san_pham)));
        data.add(new Statistic(5, context.getString(R.string.title_khach_hang),  context.getString(R.string.text_khach_hang)));
        data.add(new Statistic(6, context.getString(R.string.title_theo_thang),  context.getString(R.string.text_theo_thang)));
        statisticAdapter = new StatisticAdapter(getContext(), R.layout.activity_statistic_row, data);
        listView.setAdapter(statisticAdapter);
    }
}