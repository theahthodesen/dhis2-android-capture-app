package org.dhis2.usescases.teiDashboard;

import org.dhis2.data.dagger.PerActivity;
import org.dhis2.usescases.teiDashboard.dashboardfragments.charts.ChartsComponent;
import org.dhis2.usescases.teiDashboard.dashboardfragments.charts.ChartsModule;
import org.dhis2.usescases.teiDashboard.dashboardfragments.indicators.IndicatorsComponent;
import org.dhis2.usescases.teiDashboard.dashboardfragments.indicators.IndicatorsModule;
import org.dhis2.usescases.teiDashboard.dashboardfragments.notes.NotesComponent;
import org.dhis2.usescases.teiDashboard.dashboardfragments.notes.NotesModule;
import org.dhis2.usescases.teiDashboard.dashboardfragments.relationships.RelationshipComponent;
import org.dhis2.usescases.teiDashboard.dashboardfragments.relationships.RelationshipModule;
import org.dhis2.usescases.teiDashboard.dashboardfragments.tei_data.TEIDataComponent;
import org.dhis2.usescases.teiDashboard.dashboardfragments.tei_data.TEIDataModule;
import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import dagger.Subcomponent;

/**
 * Created by ppajuelo on 30/11/2017.
 */
@PerActivity
@Subcomponent(modules = TeiDashboardModule.class)
public interface TeiDashboardComponent {

    @NonNull
    IndicatorsComponent plus(IndicatorsModule indicatorsModule);

    @NonNull
    RelationshipComponent plus(RelationshipModule relationshipModule);

    @NonNull
    NotesComponent plus(NotesModule notesModule);

    @NotNull
    ChartsComponent plus(ChartsModule chartsModule);

    @NonNull
    TEIDataComponent plus(TEIDataModule teiDataModule);

    void inject(TeiDashboardMobileActivity mobileActivity);
}
