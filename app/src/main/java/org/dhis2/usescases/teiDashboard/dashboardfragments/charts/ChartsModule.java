package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import org.dhis2.data.dagger.PerFragment;
import org.dhis2.usescases.teiDashboard.DashboardRepository;
import org.hisp.dhis.android.core.D2;

import dagger.Module;
import dagger.Provides;

@PerFragment
@Module
public class ChartsModule{

    private final String programUid;
    private final String teiUid;


    public ChartsModule(String programUid, String teiUid){

        this.programUid = programUid;
        this.teiUid = teiUid;

    }

    @Provides
    @PerFragment
    ChartsContracts.Presenter providesPresenter(D2 d2, DashboardRepository dashboardRepository){
        return new ChartsPresenterImpl(d2, programUid, teiUid, dashboardRepository);
    }


}