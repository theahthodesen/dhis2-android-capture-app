package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.LineData;

import org.dhis2.data.tuples.Quartet;

import java.util.ArrayList;
import java.util.HashMap;

public class ChartContainer {
    public ChartsPresenterImpl presenter;
    private LineData lineData;
    private HashMap<Float,Quartet> calValues;

    public ChartContainer(LineData lineData, HashMap<Float,Quartet> calValues, ChartsPresenterImpl presenter){
        this.presenter = presenter;
        this.lineData = lineData;
        this.calValues = calValues;
    }


    public LineData getLineData(){
        return lineData;
    }

    public String zScore(float x, float y){
        System.out.println("uyuyuy: " +x + "   " + y);
        System.out.println("uyuyuy: " +calValues.get(x).val0());
        System.out.println("uyuyuy: " +calValues.get(x).val1());
        System.out.println("uyuyuy: " +calValues.get(x).val2());
        System.out.println("uyuyuy: " +calValues.get(x).val3());
        double test = Math.pow(y / ((float) calValues.get(x).val2()),((float)calValues.get(x).val1())) - 1;
        test = test / (((float) calValues.get(x).val1())*((float)calValues.get(x).val3()));

        if(test < -3) {

            //sd3neg
            float ledd1 = 1 + (float) calValues.get(x).val1() * (float) calValues.get(x).val3() * (-3);
            float sd3neg = (float) Math.pow(ledd1, (1 / (float) calValues.get(x).val1()));
            sd3neg = sd3neg * (float) calValues.get(x).val2();

            //sd23neg
            float ledd21 = 1 + (float) calValues.get(x).val1() * (float) calValues.get(x).val3() * (-2);
            ledd21 = (float) Math.pow(ledd21, (1 / (float) calValues.get(x).val1()));
            ledd21 = ledd21 * (float) calValues.get(x).val2();


            float ledd22 = 1 + (float) calValues.get(x).val1() * (float) calValues.get(x).val3() * (-3);
            ledd22 = (float) Math.pow(ledd22, (1 / (float) calValues.get(x).val1()));
            ledd22 = ledd22 * (float) calValues.get(x).val2();


            float sd23neg = -3+((y - sd3neg)/(ledd21 - ledd22));
            return String.format("%.2f", (double) sd23neg);
        }
        return String.format("%.2f", test);

    }

}
