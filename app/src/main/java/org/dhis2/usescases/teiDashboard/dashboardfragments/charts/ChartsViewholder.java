package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import org.dhis2.R;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.dhis2.databinding.ItemChartsBinding;
import org.dhis2.utils.DateUtils;
import org.hisp.dhis.android.core.event.Event;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChartsViewholder extends RecyclerView.ViewHolder {

    ItemChartsBinding binding;


    ChartsViewholder(ItemChartsBinding binding){
        super(binding.getRoot());
        this.binding = binding;

    }

    public void bind(LineData model, int days, int type, ChartContainer chartContainer){
        binding.chart.getXAxis().setGranularityEnabled(false);
        binding.chart.getDescription().setEnabled(false);
        binding.chart.getLegend().setEnabled(false);
        binding.chart.getXAxis().removeAllLimitLines();
        binding.chart.clear();
        binding.chart.setData(model);
        binding.chart.getXAxis().setDrawGridLinesBehindData(true);
        binding.chart.getXAxis().setLabelCount(20);

        binding.chart.setRenderer(new MyLineLegendRenderer(binding.chart, binding.chart.getAnimator(), binding.chart.getViewPortHandler()));
        MarkerView marker = new CustomMarkerView(binding.getRoot().getContext(),R.layout.tvcontent, chartContainer);
        binding.chart.setMarker(marker);
        binding.chart.setTouchEnabled(true);
        binding.chart.setDragEnabled(false);
        binding.chart.setScaleEnabled(true);
        if ( type <2) {
            binding.chart.getXAxis().setGranularityEnabled(true);

            binding.chart.getXAxis().setGranularity(365);

          binding.chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (((int)value)  == 0)
                    return "Birth";
                else if (((int)value) / 365 == 1)
                    return "" + ((int)value) / 365 + " year";
                else if (((int)value) % 365 == 0)
                        return "" + ((int)value) / 365 + " years";
                return "test";
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return super.getFormattedValue(value, axis);
            }
        });


        }
        if(days >= 0) {
            LimitLine dayLine = new LimitLine(days, "Today is day: " + days);
            dayLine.setLineWidth(0.2f);
            dayLine.enableDashedLine(10f, 10f, 0f);
            dayLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            dayLine.setTextSize(10f);
            binding.chart.getXAxis().addLimitLine(dayLine);
        }


        binding.executePendingBindings();
        itemView.setOnClickListener(view->{
        });
        binding.chart.setPinchZoom(true);
        binding.chart.setDragEnabled(true);
        binding.chart.invalidate();
    }

    public class CustomMarkerView extends MarkerView {
        ChartContainer chartContainer;
        private TextView tvContent;

        public CustomMarkerView (Context context, int layoutResource, ChartContainer chartContainer) {
            super(context, layoutResource);
            // this markerview only displays a textview
            tvContent = (TextView) findViewById(R.id.tvContent);.
            this.chartContainer = chartContainer;
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            tvContent.setText("x:" + e.getX() + "  y:" + e.getY() + " z-score:"+ chartContainer.zScore((int) e.getX(),e.getY())); // set the entry-value as the display text
            super.refreshContent(e, highlight);

        }
        private MPPointF mOffset;

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }

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
            final List<Entry> boundaryEntries = ((ChartsFragment.MyFillFormatter)dataSet.getFillFormatter()).getFillLineBoundary();

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

}
