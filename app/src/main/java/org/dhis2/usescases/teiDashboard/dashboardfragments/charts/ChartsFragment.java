package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.apache.commons.lang3.ArrayUtils;
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

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import timber.log.Timber;


public class ChartsFragment extends FragmentGlobalAbstract implements ChartsContracts.View {

    @Inject
    ChartsContracts.Presenter presenter;

    private ChartsAdapter adapter;
    private FragmentChartsBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);

        List<LineData> charts = new ArrayList<LineData>();
        ArrayList<ILineDataSet> dataSets = readSDValues("hfa_girls_z.txt");
        dataSets.add(addUserData());
        charts.add( new LineData(dataSets));
        charts.add( new LineData(readSDValues("wfa_girls_z.txt")));
        charts.add( new LineData(readSDValues("wfh_girls_z.txt")));
        adapter.setItems(charts);
    }

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
        binding.heightforage.setOnClickListener(this::set_height_for_age);
        binding.weightforage.setOnClickListener(this::set_weight_for_age);
        binding.weightforheight.setOnClickListener(this::set_weight_for_height);


        return binding.getRoot();
    }

    public ArrayList<ILineDataSet> readSDValues(String nameOfFile){

        BufferedReader reader;
        ArrayList<ArrayList<Entry>> datasets;
        ArrayList<ILineDataSet> sets = new ArrayList<>();

        try {
            InputStream inputStream = getResources().getAssets().open(nameOfFile);
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
            ArrayList<LineDataSet> lineDataSetList =  new ArrayList<LineDataSet>();
            for (int i = 0; i < datasets.size(); i++) {
                lineDataSetList.add(new LineDataSet(datasets.get(i),Integer.toString(i)));
            }

                for (int i = 0; i < lineDataSetList.size()-1; i++) {
                    setColor(lineDataSetList.get(i));
                    lineDataSetList.get(i).setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    lineDataSetList.get(i).setDrawCircles(false);
                    lineDataSetList.get(i).setLineWidth(0.2f);
                    lineDataSetList.get(i).setDrawCircleHole(false);
                    lineDataSetList.get(i).setFillAlpha(170);
                    lineDataSetList.get(i).setDrawFilled(true);
                    lineDataSetList.get(i).setHighlightEnabled(false);
                    lineDataSetList.get(i).setFillFormatter(new MyFillFormatter(lineDataSetList.get(i+1)));

                sets.add(lineDataSetList.get(i));

                count +=1;

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return sets;

    }
    public LineDataSet addUserData(){
        return presenter.setUserData();
    }
    public void resetTypefaceChart(){
        binding.heightforage.setTypeface(null, Typeface.NORMAL);
        binding.weightforheight.setTypeface(null, Typeface.NORMAL);
        binding.weightforage.setTypeface(null, Typeface.NORMAL);

    }
    public void set_weight_for_height(View i) {
        resetTypefaceChart();
        adapter.setchartType("weight_for_height");
        binding.weightforheight.setTypeface(null, Typeface.BOLD);

    }
    public void set_weight_for_age(View i) {
        resetTypefaceChart();
        adapter.setchartType("weight_for_age");
        binding.weightforage.setTypeface(null, Typeface.BOLD);
    }
    public void set_height_for_age(View i) {
        resetTypefaceChart();
        adapter.setchartType("height_for_age");
        binding.heightforage.setTypeface(null, Typeface.BOLD);
    }
    public LineChart setStyling(LineChart chart, List<LineDataSet> sets){

        int loop = sets.size()/2;
        int middle = Math.round(sets.size()/2);
        for(int i = 1; i < loop; i++){
            int c = getColor(i);
            sets.get(middle+i).setFillColor(c);
            sets.get(middle+i).setDrawFilled(true);
            sets.get(middle-i).setFillColor(c);
            sets.get(middle-i).setDrawFilled(true);
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
        if(dataset.getLabel().contains("3") || dataset.getLabel().contains("4")){
            dataset.setFillColor(Color.GREEN);
        }
        else if(dataset.getLabel().contains("2") || dataset.getLabel().contains("5")){
            dataset.setFillColor(Color.YELLOW);
        }
        else if(dataset.getLabel().contains("1") || dataset.getLabel().contains("6")){
            dataset.setFillColor(Color.rgb(255,215,0));
        }
        else if(dataset.getLabel().contains("0") || dataset.getLabel().contains("7")){
            dataset.setFillColor(Color.RED);
        }
        else{
            dataset.setFillColor(Color.WHITE);
        }
        return dataset;

    }
    public class MyFillFormatter implements IFillFormatter {
        private ILineDataSet boundaryDataSet;

        public MyFillFormatter() {
            this(null);
        }
        //Pass the dataset of other line in the Constructor
        public MyFillFormatter(ILineDataSet boundaryDataSet) {
            this.boundaryDataSet = boundaryDataSet;
        }

        @Override
        public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
            return 0;
        }

        //Define a new method which is used in the LineChartRenderer
        public List<Entry> getFillLineBoundary() {
            if(boundaryDataSet != null) {
                return ((LineDataSet) boundaryDataSet).getValues();
            }
            return null;
        }}

    public class MyLineLegendRenderer extends LineChartRenderer {

        MyLineLegendRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
            super(chart, animator, viewPortHandler);
        }

        // This method is same as its parent implementation. (Required so our version of generateFilledPath() is called.)
        @Override
        protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {

            final Path filled = mGenerateFilledPathBuffer;

            final int startingIndex = bounds.min;
            final int endingIndex = bounds.range + bounds.min;
            final int indexInterval = 128;

            int currentStartIndex;
            int currentEndIndex;
            int iterations = 0;

            // Doing this iteratively in order to avoid OutOfMemory errors that can happen on large bounds sets.
            do {
                currentStartIndex = startingIndex + (iterations * indexInterval);

                currentEndIndex = currentStartIndex + indexInterval;
                currentEndIndex = currentEndIndex > endingIndex ? endingIndex : currentEndIndex;

                if (currentStartIndex <= currentEndIndex) {
                    generateFilledPath(dataSet, currentStartIndex, currentEndIndex, filled);

                    trans.pathValueToPixel(filled);

                    final Drawable drawable = dataSet.getFillDrawable();
                    if (drawable != null) {
                        drawFilledPath(c, filled, drawable);
                    }
                    else {
                        drawFilledPath(c, filled, dataSet.getFillColor(), dataSet.getFillAlpha());
                    }
                }

                iterations++;

            } while (currentStartIndex <= currentEndIndex);
        }

        // This method defines the perimeter of the area to be filled for horizontal bezier data sets.
        @Override
        protected void drawCubicFill(Canvas c, ILineDataSet dataSet, Path spline, Transformer trans, XBounds bounds) {

            final float phaseY = mAnimator.getPhaseY();

            //Call the custom method to retrieve the dataset for other line
            final List<Entry> boundaryEntries = ((MyFillFormatter)dataSet.getFillFormatter()).getFillLineBoundary();

            // We are currently at top-last point, so draw down to the last boundary point
            Entry boundaryEntry = boundaryEntries.get(bounds.min + bounds.range);
            spline.lineTo(boundaryEntry.getX(), boundaryEntry.getY() * phaseY);

            // Draw a cubic line going back through all the previous boundary points
            Entry prev = dataSet.getEntryForIndex(bounds.min + bounds.range);
            Entry cur = prev;
            for (int x = bounds.min + bounds.range; x >= bounds.min; x--) {

                prev = cur;
                cur = boundaryEntries.get(x);

                final float cpx = (prev.getX()) + (cur.getX() - prev.getX()) / 2.0f;

                spline.cubicTo(
                        cpx, prev.getY() * phaseY,
                        cpx, cur.getY() * phaseY,
                        cur.getX(), cur.getY() * phaseY);
            }

            // Join up the perimeter
            spline.close();

            trans.pathValueToPixel(spline);

            final Drawable drawable = dataSet.getFillDrawable();
            if (drawable != null) {
                drawFilledPath(c, spline, drawable);
            }
            else {
                drawFilledPath(c, spline, dataSet.getFillColor(), dataSet.getFillAlpha());
            }

        }

        // This method defines the perimeter of the area to be filled for straight-line (default) data sets.
        private void generateFilledPath(final ILineDataSet dataSet, final int startIndex, final int endIndex, final Path outputPath) {

            final float phaseY = mAnimator.getPhaseY();
            final Path filled = outputPath; // Not sure if this is required, but this is done in the original code so preserving the same technique here.
            filled.reset();

            //Call the custom method to retrieve the dataset for other line
            final List<Entry> boundaryEntries = ((MyFillFormatter)dataSet.getFillFormatter()).getFillLineBoundary();

            final Entry entry = dataSet.getEntryForIndex(startIndex);
            final Entry boundaryEntry = boundaryEntries.get(startIndex);

            // Move down to boundary of first entry
            filled.moveTo(entry.getX(), boundaryEntry.getY() * phaseY);

            // Draw line up to value of first entry
            filled.lineTo(entry.getX(), entry.getY() * phaseY);

            // Draw line across to the values of the next entries
            Entry currentEntry;
            for (int x = startIndex + 1; x <= endIndex; x++) {
                currentEntry = dataSet.getEntryForIndex(x);
                filled.lineTo(currentEntry.getX(), currentEntry.getY() * phaseY);
            }

            // Draw down to the boundary value of the last entry, then back to the first boundary value
            Entry boundaryEntry1;
            for (int x = endIndex; x > startIndex; x--) {
                boundaryEntry1 = boundaryEntries.get(x);
                filled.lineTo(boundaryEntry1.getX(), boundaryEntry1.getY() * phaseY);
            }

            // Join up the perimeter
            filled.close();

        }

    }

    //public Consumer<List<LineChart>> swapCharts() {



        //return chartModels -> adapter.setItems(chartModels);
    //}
}
