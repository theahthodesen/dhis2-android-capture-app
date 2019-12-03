package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import android.util.Pair;

import androidx.annotation.NonNull;


public class RutfRation {

    public Pair<String, String> get92g(float weight) {
        if (ifBetween(3.5, 4, weight))
            return new Pair<String, String>("10", "1.5");
        else if (ifBetween(4, 4.5, weight))
            return new Pair<String, String>("11", "1.5");
        else if (ifBetween(4.5, 5, weight))
            return new Pair<String, String>("12", "1.75");
        else if (ifBetween(5, 6, weight))
            return new Pair<String, String>("14", "2");
        else if (ifBetween(6, 7, weight))
            return new Pair<String, String>("17", "2.5");
        else if (ifBetween(7, 8, weight))
            return new Pair<String, String>("20", "3");
        else if (ifBetween(8, 9, weight))
            return new Pair<String, String>("23", "3.25");
        else if (ifBetween(9, 10, weight))
            return new Pair<String, String>("26", "3.75");
        else if (ifBetween(10, 12, weight))
            return new Pair<String, String>("28", "4");
        else if (ifBetween(12, 15, weight))
            return new Pair<String, String>("35", "5");
        else if (ifBetween(15, 20, weight))
            return new Pair<String, String>("45", "6.5");
        else if (ifBetween(20, 25, weight))
            return new Pair<String, String>("60", "8.5");
        return null;
    }

    public boolean ifBetween(double i1, double i2, float val) {
        return (i1 >= val && val < i2);
    }
}
