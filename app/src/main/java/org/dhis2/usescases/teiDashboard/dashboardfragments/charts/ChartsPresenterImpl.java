package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import com.github.mikephil.charting.data.Entry;


import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.dhis2.usescases.teiDashboard.DashboardRepository;
import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.datavalue.DataValue;
import org.hisp.dhis.android.core.enrollment.Enrollment;

import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.program.ProgramTrackedEntityAttribute;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValue;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.android.core.dataelement.DataElement;
import org.hisp.dhis.android.core.enrollment.EnrollmentCollectionRepository;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


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

    public String getGender() {
        String gender;
        TrackedEntityAttributeValue genderData = d2.trackedEntityModule().trackedEntityAttributeValues().byTrackedEntityInstance().in(teiUid)
                .byTrackedEntityAttribute().in("cejWyOfXge6").one().blockingGet();

        if( genderData == null){
            gender = "Male";
        }
        else{
            gender = genderData.value();
        }

        return  gender;
    }



    public List<Entry> importChild(int chartType){

        List<Entry> entries = new ArrayList<>();
        List<Event> events = d2.eventModule().events().byEnrollmentUid().eq(d2.enrollmentModule().enrollments().byProgram().eq(programUid)
                .byTrackedEntityInstance().eq(teiUid).one().blockingGet().uid()).byProgramStageUid().eq("Hj9JdKUS4Hj").blockingGet();

        DataElement heightDataElement = d2.dataElementModule().dataElements().byUid().eq("Gm634az8FEU").one().blockingGet();
        DataElement weightDataElement = d2.dataElementModule().dataElements().byUid().eq("GZZLGtLZwep").one().blockingGet();

        switch (chartType) {
            case 1:
                for(Event e : events) {

                    TrackedEntityDataValue height = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(heightDataElement.uid()).one().blockingGet();
                    String h = height.value();

                    Date d = height.created();

                    Enrollment birthdate = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid).byProgram().eq(programUid).one().blockingGet();
                    Date bd = birthdate.incidentDate();


                    long diff = d.getTime() - bd.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = (hours / 24) + 1;

                    entries.add(new Entry(days, Integer.parseInt(h)));
                }
                break;

            case 2:
                for(Event e : events){

                    TrackedEntityDataValue weight = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(weightDataElement.uid()).one().blockingGet();
                    String w = weight.value();


                    Date d = weight.created();

                    Enrollment birthdate = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid).byProgram().eq(programUid).one().blockingGet();
                    Date bd = birthdate.incidentDate();



                    long diff = d.getTime() - bd.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = (hours / 24) + 1;

                    entries.add(new Entry(days, Integer.parseInt(w)));

                }

                break;

            case 3:

                for(Event e : events){

                    TrackedEntityDataValue height = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(heightDataElement.uid()).one().blockingGet();
                    String h = height.value();

                    TrackedEntityDataValue weight = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(weightDataElement.uid()).one().blockingGet();
                    String w = weight.value();

                    entries.add(new Entry(Integer.parseInt(h), Integer.parseInt(w)));

                }
                break;
        }
        return entries;


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
