package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import org.dhis2.usescases.teiDashboard.DashboardRepository;
import org.hisp.dhis.android.core.D2;

import io.reactivex.disposables.CompositeDisposable;

public class ChartsPresenterImpl implements ChartsContracts.Presenter{

    private final D2 d2;
    private CompositeDisposable compositeDisposable;
    private final String programUid;
    private final DashboardRepository dashboardRepository;
    private ChartsContracts.View view;
    private String teiUid;

    @Override
    public boolean hasProgramWritePermission() {
        return d2.programModule().programs.uid(programUid).withAllChildren().blockingGet().access().data().write();

    }

    public ChartsPresenterImpl(D2 d2, String programUid, String teiUid, DashboardRepository dashboardRepository) {

        this.d2 = d2;
        this.dashboardRepository = dashboardRepository;
        this.programUid = programUid;
        this.teiUid = teiUid;
    }

    @Override
    public void init(ChartsContracts.View view) {
        this.view = view;
        this.compositeDisposable = new CompositeDisposable();
    }


    @Override
    public void onDettach() {
        compositeDisposable.clear();
    }

    @Override
    public void displayMessage(String message) {
        view.displayMessage(message);
    }
}
