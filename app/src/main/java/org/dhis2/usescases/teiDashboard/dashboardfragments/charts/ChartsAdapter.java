package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.dhis2.R;
import org.dhis2.databinding.ItemChartsBinding;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

class ChartsAdapter extends RecyclerView.Adapter<ChartsViewholder>{

    String chartType;
    private int days;
    private ArrayList<ChartContainer> listOfCharts;

    public ChartsAdapter(int days){
        chartType = "height_for_age";
        this.days = days;
        listOfCharts = new ArrayList<>();
    }
    public void addChart(ChartContainer chart) {
        listOfCharts.add(chart);
    }
    @NotNull
    @Override
    public ChartsViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        ItemChartsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_charts, parent, false);
        return new ChartsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartsViewholder holder, int position) {
        holder.bind(listOfCharts.get(chartType(chartType)).getLineData(), days, chartType(chartType), listOfCharts.get(chartType(chartType)));
    }
    @Override
    public int getItemCount() {
        return 1;
    }

    public void setDays(int i) {
        days = i;
    }

    public void setchartType(String type) {
        chartType = type;
        notifyDataSetChanged();

    }

    public int chartType(String type){
        int i = -1;
        switch (type) {
            case "height_for_age":
                i = 0;
                break;
            case "weight_for_age":
                i = 1;
                break;
            case "weight_for_height":
                i = 2;
                break;
        }
        return i;
    }



}
