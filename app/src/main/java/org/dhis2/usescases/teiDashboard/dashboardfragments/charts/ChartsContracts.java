package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import org.dhis2.usescases.general.AbstractActivityContracts;

import java.util.List;
import java.util.function.Consumer;

public class ChartsContracts {

    public interface View extends AbstractActivityContracts.View {

        //Consumer<List<ChartModel>> swapCharts();


    }


    public interface Presenter extends AbstractActivityContracts.Presenter {

        void init(ChartsContracts.View view);
        boolean hasProgramWritePermission();


    }

}
