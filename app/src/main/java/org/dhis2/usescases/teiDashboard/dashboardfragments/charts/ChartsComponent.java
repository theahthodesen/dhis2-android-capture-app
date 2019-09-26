package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import org.dhis2.data.dagger.PerFragment;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = ChartsModule.class)
public interface ChartsComponent {

    void inject(ChartsFragment chartsFragment);
}
