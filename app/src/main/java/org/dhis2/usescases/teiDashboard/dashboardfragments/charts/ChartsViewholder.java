package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import com.github.mikephil.charting.charts.LineChart;

import org.dhis2.databinding.ItemChartsBinding;
import org.dhis2.utils.DateUtils;
import org.hisp.dhis.android.core.enrollment.chart.ChartModel;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ChartsViewholder extends RecyclerView.ViewHolder {

    ItemChartsBinding binding;


    ChartsViewholder(ItemChartsBinding binding){
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(LineChart model){

        binding.executePendingBindings();
        itemView.setOnClickListener(view->{

        });


    }
}
