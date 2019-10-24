package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.dhis2.App;
import org.dhis2.R;
import org.dhis2.databinding.FragmentChartsBinding;
import org.dhis2.usescases.general.FragmentGlobalAbstract;
import org.dhis2.usescases.sync.SyncContracts;
import org.dhis2.usescases.teiDashboard.TeiDashboardMobileActivity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.functions.Consumer;
import timber.log.Timber;


public class ChartsFragment extends FragmentGlobalAbstract implements ChartsContracts.View {

    ChartsContracts.Presenter presenter;
    private ChartsAdapter adapter;
    private FragmentChartsBinding binding;



    public void onAttach(Context context){
        super.onAttach(context);
        TeiDashboardMobileActivity activity = (TeiDashboardMobileActivity) context;
        if (((App) context.getApplicationContext()).dashboardComponent() != null)
            ((App) context.getApplicationContext())
                    .dashboardComponent()
                    .plus(new ChartsModule(activity.getProgramUid(), activity.getTeiUid()))
                    .inject(this);
    }

    public void addChart(View view) {
        if (!presenter.hasProgramWritePermission()) {

            displayMessage(getString(R.string.search_access_error));
        } else {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charts, container, false);
        adapter = new ChartsAdapter();
        binding.chartsRecycler.setAdapter(adapter);
        LineChart chart = binding.getRoot().findViewById(R.id.chart);
        ArrayList<LineDataSet> setForStyling = new ArrayList<>();
        /*int count = 0;

        Integer[] dataAge = {1, 2, 4, 6, 10, 12};
        double [] dataWeight = {3.5, 3.6, 3.2, 4, 4.2, 5};

        Integer[] SDnormalHA = {1, 2, 4, 6, 10, 12};
        double [] SDnormalHW = {4.2, 4.5, 4.7, 4.9, 5, 5.7};

        Integer[] SDnormalLA = {1, 2, 4, 6, 10, 12};
        double [] SDnormalLW = {3.3, 3.5, 3.7, 4.2, 4.5, 4.7};

        List<Entry> entries = new ArrayList<>();
        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();

        for (Integer age : dataAge) {

            entries.add(new Entry((float)age, (float)dataWeight[count]));
            entries1.add(new Entry((float)SDnormalHA[count], (float)SDnormalHW[count]));
            entries2.add(new Entry((float)SDnormalLA[count], (float)SDnormalLW[count]));
            count++;
        }

        LineDataSet dataSet1 = new LineDataSet(entries1, "SD normal high");
        LineDataSet dataSet2 = new LineDataSet(entries2, "SD normal low");


        dataSet1.setFillAlpha(255);
        dataSet1.setDrawFilled(true);
        dataSet1.setDrawCircles(false);
        dataSet1.setFillColor(Color.GREEN);
        dataSet1.setColor(Color.GREEN);
        dataSet1.setDrawValues(false);
        dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet2.setFillAlpha(255);
        dataSet2.setDrawFilled(true);
        dataSet2.setFillColor(Color.WHITE);
        dataSet2.setColor(Color.GREEN);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawValues(false);
        dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        LineDataSet dataSet = new LineDataSet(entries, "Weight for age");
        dataSet.setCircleColor(Color.BLACK);
        dataSet.setDrawFilled(true);
        dataSet.setColor(Color.BLACK);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        dataSets.add(dataSet);
        LineData data  = new LineData(dataSets);*/

        LineData data = new LineData(readSDValues());

        chart.setData(data);
        chart.getXAxis().setDrawGridLinesBehindData(true);

        chart.invalidate();

        return binding.getRoot();
    }

    public ArrayList<ILineDataSet> readSDValues(){

        BufferedReader reader;
        ArrayList<ArrayList<Entry>> datasets;
        ArrayList<ILineDataSet> sets = new ArrayList<>();

        try {
            InputStream inputStream = getResources().getAssets().open("wfa_girls_z.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            String[] labels = line.split("\t");
            datasets = new ArrayList<>(labels.length-1);
            for(int i = 0; i < labels.length-1; i++){
                datasets.add(new ArrayList<Entry>());
            }
            line = reader.readLine();
            while (line != null){
                String[] values = line.split("\t");
                int c = 1;
                for(ArrayList<Entry> e : datasets){
                    e.add(new Entry(Float.parseFloat(values[0]), Float.parseFloat(values[c])));
                    c+=1;

                }
                line = reader.readLine();
            }
            int count = 1;
            for(ArrayList<Entry> entries : datasets) {

                LineDataSet dataset = new LineDataSet(entries, labels[count]);
                dataset = setColor(dataset);
                sets.add(dataset);

                count +=1;

            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        return sets;



    }

    public LineChart setStyling(LineChart chart, List<LineDataSet> sets){


        int loop = sets.size()/2;
        int middle = Math.round(sets.size()/2);
        for(int i = 1; i < loop; i++){
            int c = getColor(i);
            sets.get(middle+i).setFillColor(c);
            sets.get(middle-i).setFillColor(c);
            sets.get(middle+i).setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                // change the return value here to better understand the effect
                //  return 600;
                return chart.getAxisLeft().getAxisMaximum();
            }});

            sets.get(middle-i).setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    // change the return value here to better understand the effect
                    //  return 600;
                    return chart.getAxisLeft().getAxisMinimum();
                }});
        }

        return chart;
    }

    public int getColor(int i) {
        int color = Color.GREEN;
        switch(i) {
            case 1: color = Color.YELLOW;
                break;
            case 2: color = Color.rgb(255,215,0);
                break;
            case 3: color = Color.RED;
                break;
        }
        return color;
    }

    public LineDataSet setColor(LineDataSet dataset){
        if(dataset.getLabel().contains("1") || dataset.getLabel().contains("0")){
            dataset.setFillColor(Color.GREEN);
        }
        else if(dataset.getLabel().contains("2")){
            dataset.setFillColor(Color.YELLOW);
        }
        else if(dataset.getLabel().contains("3")){
            dataset.setFillColor(Color.rgb(255,215,0));
        }
        else if(dataset.getLabel().contains("4")){
            dataset.setFillColor(Color.RED);
        }
        else{
            dataset.setFillColor(Color.WHITE);
        }
        return dataset;

    }





    //public Consumer<List<LineChart>> swapCharts() {



        //return chartModels -> adapter.setItems(chartModels);
    //}
}
