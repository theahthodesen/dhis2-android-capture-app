package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.LineData;

import org.dhis2.data.tuples.Quartet;

import java.util.ArrayList;

public class ChartContainer {
    private LineData lineData;
    private ArrayList<Quartet> calValues;

    public ChartContainer(LineData lineData, ArrayList<Quartet> calValues){
        this.lineData = lineData;
        this.calValues = calValues;
    }
    public LineData getLineData(){
        return lineData;
    }

    public String zScore(int x, float y){
        double test = Math.pow(y / ((float) calValues.get(x).val2()),((float)calValues.get(x).val1())) - 1;
        test = test / (((float) calValues.get(x).val1())*((float)calValues.get(x).val3()));
        return String.format("%.2f", test);
    }

}
