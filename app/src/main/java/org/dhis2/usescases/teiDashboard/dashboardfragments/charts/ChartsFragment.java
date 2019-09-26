package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.dhis2.App;
import org.dhis2.R;
import org.dhis2.databinding.FragmentChartsBinding;
import org.dhis2.usescases.general.FragmentGlobalAbstract;
import org.dhis2.usescases.teiDashboard.TeiDashboardMobileActivity;
import org.hisp.dhis.android.core.enrollment.chart.ChartModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import io.reactivex.functions.Consumer;


public class ChartsFragment extends FragmentGlobalAbstract implements ChartsContracts.View {

    ChartsContracts.Presenter presenter;
    private ChartsAdapter adapter;
    private FragmentChartsBinding binding;


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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charts, container, false);
        adapter = new ChartsAdapter();
        binding.chartsRecycler.setAdapter(adapter);
        return binding.getRoot();
    }

    /*@Override
    public Consumer<List<ChartModel>> swapCharts() {
        return chartModels -> adapter.setItems(chartModels);
    }*/
}
