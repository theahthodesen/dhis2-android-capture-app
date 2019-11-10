package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.dhis2.usescases.teiDashboard.DashboardRepository;
import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.dataelement.DataElement;
import org.hisp.dhis.android.core.enrollment.EnrollmentCollectionRepository;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import static android.text.TextUtils.isEmpty;

public class ChartsPresenterImpl implements ChartsContracts.Presenter{

    private final D2 d2;
    private CompositeDisposable compositeDisposable;
    private final String programUid;
    private final DashboardRepository dashboardRepository;
    private ChartsContracts.View view;
    private String teiUid;

    @Override
    public boolean hasProgramWritePermission() {
        return d2.programModule().programs().uid(programUid).blockingGet().access().data().write();

    }

    public ChartsPresenterImpl(D2 d2, String programUid, String teiUid, DashboardRepository dashboardRepository) {

        this.d2 = d2;
        this.dashboardRepository = dashboardRepository;
        this.programUid = programUid;
        this.teiUid = teiUid;
    }
    public LineDataSet setUserData() {
        EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid);
        enrollmentRepository = enrollmentRepository.byProgram().eq(programUid);
        String enrollmentUid = enrollmentRepository.one().blockingGet().uid();

        List<Event> events = d2.eventModule().events().byEnrollmentUid().eq(enrollmentUid).byProgramUid().eq(programUid).blockingGet();
        DataElement heightElement = d2.dataElementModule().dataElements().byUid().eq("Gm634az8FEU").one().blockingGet();
        heightElement.name();
        List<String> list = new ArrayList<String>();
        for (Event event : events) {
            TrackedEntityDataValue temp = d2.trackedEntityModule().
                    trackedEntityDataValues().
                    byEvent().eq(event.uid()).
                    byDataElement().
                    in(heightElement.
                            uid()).
                    one().
                    blockingGet();
            list.add(temp.value());
        }
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            float val = (float) Float.parseFloat(list.get(i));
            values.add(new Entry(i*100, val));
        }
        LineDataSet linedataset = new LineDataSet(values, "tester");
        linedataset.setColor(Color.BLACK);
        return new LineDataSet(values, "tester");
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
