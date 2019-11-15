package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import com.github.mikephil.charting.data.Entry;


import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.dhis2.usescases.teiDashboard.DashboardRepository;
import org.dhis2.utils.DateUtils;
import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.enrollment.Enrollment;

import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.event.EventCreateProjection;
import org.hisp.dhis.android.core.event.EventObjectRepository;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.android.core.dataelement.DataElement;
import org.hisp.dhis.android.core.enrollment.EnrollmentCollectionRepository;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttribute;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public String createEvent() {
        Calendar cal = Calendar.getInstance();
        String eventUid ="";
        try {
            cal.setTime(DateUtils.getInstance().getCalendar().getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);


            EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid);
            enrollmentRepository = enrollmentRepository.byProgram().eq(programUid);
            String enrollmentUid = enrollmentRepository.one().blockingGet().uid();

             eventUid = d2.eventModule().events().blockingAdd(
                    EventCreateProjection.create(enrollmentUid, programUid, "Hj9JdKUS4Hj", "DiszpKrYNg8", null));
            EventObjectRepository eventRepository = d2.eventModule().events().uid(eventUid);
            eventRepository.setEventDate(cal.getTime());
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
        return eventUid;

    }
    public String getProgramUid(){
        return programUid;
    }
public void test(){  Calendar cal = Calendar.getInstance();

    try {
        cal.setTime(DateUtils.getInstance().getCalendar().getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid);
        enrollmentRepository = enrollmentRepository.byProgram().eq(programUid);
        String enrollmentUid = enrollmentRepository.one().blockingGet().uid();

        String eventUid = d2.eventModule().events().blockingAdd(
                EventCreateProjection.create(enrollmentUid, programUid, "Hj9JdKUS4Hj", "DiszpKrYNg8", null));
        EventObjectRepository eventRepository = d2.eventModule().events().uid(eventUid);
        eventRepository.setEventDate(cal.getTime());

        DataElement heightDataElement = d2.dataElementModule().dataElements().byUid().eq("Gm634az8FEU").one().blockingGet();

        d2.trackedEntityModule().trackedEntityDataValues().value(eventUid, heightDataElement.uid()).blockingSet("80");

    } catch (D2Error d2Error) {
        d2Error.printStackTrace();
    }
}

    @Override
    public void init(ChartsContracts.View view) {
        this.view = view;
        this.compositeDisposable = new CompositeDisposable();

    }
    public int getTodayInt(int chartType){
        if (chartType == 1 || chartType == 2) {
            EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid).byProgram().eq(programUid);
            Date incidentDate = enrollmentRepository.one().blockingGet().incidentDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtils.getInstance().getCalendar().getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long diff = cal.getTime().getTime() - incidentDate.getTime();
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } else return -1;
    }
    public List<Entry> importChild(int chartType){
        EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid).byProgram().eq(programUid);
        String enrollmentUid = enrollmentRepository.one().blockingGet().uid();
        Date incidentDate = enrollmentRepository.one().blockingGet().incidentDate();

        List<Entry> entries = new ArrayList<>();
        List<Event> events = d2.eventModule().events().byEnrollmentUid().eq(enrollmentUid).byProgramStageUid().eq("Hj9JdKUS4Hj").blockingGet();
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return ( o1.eventDate().compareTo(o2.eventDate()));
            }
        });


        DataElement heightDataElement = d2.dataElementModule().dataElements().byUid().eq("Gm634az8FEU").one().blockingGet();
        DataElement weightDataElement = d2.dataElementModule().dataElements().byUid().eq("GZZLGtLZwep").one().blockingGet();
        DataElement dateDataElement = d2.dataElementModule().dataElements().byUid().eq("UpidRR4PfXv").one().blockingGet();

        switch (chartType) {
            case 1:
                for(Event e : events) {

                    TrackedEntityDataValue height = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(heightDataElement.uid()).one().blockingGet();
                    String h = height.value();
                    Date d = e.eventDate();

                    long diff = d.getTime() - incidentDate.getTime();
                    entries.add(new Entry((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS), Integer.parseInt(h)));
                }
                break;

            case 2:
                for(Event e : events){

                    TrackedEntityDataValue weight = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(weightDataElement.uid()).one().blockingGet();
                    String w = weight.value();
                    Date d = e.eventDate();

                    long diff = d.getTime() - incidentDate.getTime();
                    entries.add(new Entry((int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS), Integer.parseInt(w)));
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
