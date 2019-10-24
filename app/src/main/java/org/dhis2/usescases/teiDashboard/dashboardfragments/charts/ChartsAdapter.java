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

    private List<LineChart> charts;

    public int getItemCount(){
        return charts.size();
    }

    public ChartsAdapter(){
        this.charts = new ArrayList<>();
    }

    @NotNull
    @Override
    public ChartsViewholder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        ItemChartsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_charts, parent, false);
        return new ChartsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartsViewholder holder, int position) {
        holder.bind(charts.get(position));

    }

    public void setItems(ChartsViewholder viewholder){
        this.charts = charts;
        notifyDataSetChanged();
    }




}
