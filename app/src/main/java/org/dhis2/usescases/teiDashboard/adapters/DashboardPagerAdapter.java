package org.dhis2.usescases.teiDashboard.adapters;

import android.content.Context;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.dhis2.R;
import org.dhis2.usescases.teiDashboard.dashboardfragments.charts.ChartsFragment;
import org.dhis2.usescases.teiDashboard.dashboardfragments.indicators.IndicatorsFragment;
import org.dhis2.usescases.teiDashboard.dashboardfragments.notes.NotesFragment;
import org.dhis2.usescases.teiDashboard.dashboardfragments.relationships.RelationshipFragment;
import org.dhis2.usescases.teiDashboard.dashboardfragments.tei_data.TEIDataFragment;
import org.jetbrains.annotations.NotNull;

/**
 * QUADRAM. Created by ppajuelo on 29/11/2017.
 */

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {

    private static final int MOVILE_DASHBOARD_SIZE = 5;
    private String currentProgram;
    private Context context;


    public DashboardPagerAdapter(Context context, FragmentManager fm, String program) {
        super(fm);
        this.context = context;
        this.currentProgram = program;
    }

    @Override
    public Parcelable saveState() {
        // Do Nothing
        return null;
    }

    private IndicatorsFragment indicatorsFragment;
    private RelationshipFragment relationshipFragment;
    private NotesFragment notesFragment;
    private TEIDataFragment teiDataFragment;
    private ChartsFragment chartsFragment;

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                if (indicatorsFragment == null){
                    indicatorsFragment = new IndicatorsFragment();
                }
                return indicatorsFragment;
            case 2:
                if (relationshipFragment == null){
                    relationshipFragment = new RelationshipFragment();
                }
                return relationshipFragment;
            case 3:
                if (notesFragment == null){
                    notesFragment = new NotesFragment();
                }
                return notesFragment;
            case 4:
                if (chartsFragment == null){
                    chartsFragment = new ChartsFragment();
                }
                return chartsFragment;
            default:
                if (teiDataFragment == null){
                    teiDataFragment = new TEIDataFragment();
                }
                return teiDataFragment;
        }
    }

    @Override
    public int getCount() {
        return currentProgram != null ? MOVILE_DASHBOARD_SIZE : 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            default:
                return context.getString(R.string.dashboard_overview);
            case 1:
                return context.getString(R.string.dashboard_indicators);
            case 2:
                return context.getString(R.string.dashboard_relationships);
            case 3:
                return context.getString(R.string.dashboard_notes);
            case 4:
                return context.getString(R.string.dashboard_charts);
        }
    }
}
