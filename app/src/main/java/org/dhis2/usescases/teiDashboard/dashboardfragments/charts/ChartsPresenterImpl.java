package org.dhis2.usescases.teiDashboard.dashboardfragments.charts;


import com.github.mikephil.charting.data.Entry;


import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.dhis2.data.tuples.Pair;
import org.dhis2.usescases.teiDashboard.DashboardRepository;
import org.dhis2.utils.DateUtils;
import org.hisp.dhis.android.core.D2;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.datavalue.DataValue;
import org.hisp.dhis.android.core.enrollment.Enrollment;

import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.event.EventCreateProjection;
import org.hisp.dhis.android.core.event.EventObjectRepository;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValue;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import static android.text.TextUtils.isEmpty;

public class ChartsPresenterImpl implements ChartsContracts.Presenter {

    private final D2 d2;
    private CompositeDisposable compositeDisposable;
    private final String programUid;
    private final DashboardRepository dashboardRepository;
    private ChartsContracts.View view;
    private String teiUid;
    private Date lastDataEntry;
    private Date incidentDate;
    String enrollmentUid;
    HashMap<Integer,Date> intToDate;
    HashMap<Float,Date> intToDateWFH;
    HashMap<Integer, String> muacmap;
    HashMap<Float, String> muacmapWFH;
    HashMap<Integer, Float> rutfmap;
    HashMap<Float, Float> rutfmapWFH;


    public String uo() {
        return enrollmentUid;
    }

    public String tei() {
        return teiUid;
    }

    @Override
    public boolean hasProgramWritePermission() {
        return d2.programModule().programs().uid(programUid).blockingGet().access().data().write();

    }

    public String getLastEntryDayText() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(DateUtils.getInstance().getCalendar().getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long diff = cal.getTime().getTime() - lastDataEntry.getTime();
        return String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    public String getLastEntryDateText() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//formating according to my need
        String date = formatter.format(lastDataEntry);
        return date;
    }

    public ChartsPresenterImpl(D2 d2, String programUid, String teiUid, DashboardRepository dashboardRepository) {
        this.d2 = d2;
        this.dashboardRepository = dashboardRepository;
        this.programUid = programUid;
        this.teiUid = teiUid;
        intToDate = new HashMap<>();
        muacmap = new HashMap<>();
        muacmapWFH = new HashMap<>();
        intToDateWFH = new HashMap<>();
        rutfmap = new HashMap<>();
        rutfmapWFH = new HashMap<>();
    }

    public void muacOd(){
        EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid).byProgram().eq(programUid);
        String enrollmentUid = enrollmentRepository.one().blockingGet().uid();
        Date incidentDate = enrollmentRepository.one().blockingGet().incidentDate();
        this.incidentDate = incidentDate;
        List<Entry> entries = new ArrayList<>();
        List<Event> events = d2.eventModule().events().byEnrollmentUid().eq(enrollmentUid).byProgramStageUid().eq("Hj9JdKUS4Hj").blockingGet();
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return (o1.eventDate().compareTo(o2.eventDate()));
            }
        });


        DataElement muac = d2.dataElementModule().dataElements().byUid().eq("FQC0FrFgLOX").one().blockingGet();
        DataElement od = d2.dataElementModule().dataElements().byUid().eq("H3uHA1L4ND5").one().blockingGet();
        DataElement dateDataElement = d2.dataElementModule().dataElements().byUid().eq("UpidRR4PfXv").one().blockingGet();

    }

    public String createEvent() {
        Calendar cal = Calendar.getInstance();
        String eventUid = "";
        try {
            cal.setTime(DateUtils.getInstance().getCalendar().getTime());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);


            EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid);
            enrollmentRepository = enrollmentRepository.byProgram().eq(programUid);
            String enrollmentUid = enrollmentRepository.one().blockingGet().uid();
            this.enrollmentUid = enrollmentUid;
            eventUid = d2.eventModule().events().blockingAdd(
                    EventCreateProjection.create(enrollmentUid, programUid, "Hj9JdKUS4Hj", "DiszpKrYNg8", null));
            EventObjectRepository eventRepository = d2.eventModule().events().uid(eventUid);
            eventRepository.setEventDate(cal.getTime());
        } catch (D2Error d2Error) {
            d2Error.printStackTrace();
        }
        return eventUid;

    }

    public String getProgramUid() {
        return programUid;
    }

    public void test() {
        Calendar cal = Calendar.getInstance();

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

    public int getTodayInt(int chartType) {
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
        EnrollmentCollectionRepository enrollmentRepository = d2.enrollmentModule().enrollments().byTrackedEntityInstance().eq(teiUid).byProgram().eq(programUid);
        String enrollmentUid = enrollmentRepository.one().blockingGet().uid();
        Date incidentDate = enrollmentRepository.one().blockingGet().incidentDate();
        this.incidentDate = incidentDate;
        List<Entry> entries = new ArrayList<>();
        List<Event> events = d2.eventModule().events().byEnrollmentUid().eq(enrollmentUid).byProgramStageUid().eq("Hj9JdKUS4Hj").blockingGet();
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return (o1.eventDate().compareTo(o2.eventDate()));
            }
        });
        lastDataEntry =incidentDate;
        if(events.size() > 0)
        lastDataEntry = events.get(events.size() - 1).eventDate();

        DataElement heightDataElement = d2.dataElementModule().dataElements().byUid().eq("Gm634az8FEU").one().blockingGet();
        DataElement weightDataElement = d2.dataElementModule().dataElements().byUid().eq("GZZLGtLZwep").one().blockingGet();
        DataElement muacDataElement = d2.dataElementModule().dataElements().byUid().eq("FQC0FrFgLOX").one().blockingGet();

        switch (chartType) {
            case 1:
                for (Event e : events) {

                    TrackedEntityDataValue height = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(heightDataElement.uid()).one().blockingGet();
                    String h = height.value();
                    if (h != null) {
                        Date d = e.eventDate();

                        long diff = d.getTime() - incidentDate.getTime();
                        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        intToDate.put(days,d);
                        entries.add(new Entry(days, Float.parseFloat(h)));
                    }
                    TrackedEntityDataValue muac = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(muacDataElement.uid()).one().blockingGet();
                    String m = muac.value();
                    if (m != null) {
                        Date d = e.eventDate();

                        long diff = d.getTime() - incidentDate.getTime();
                        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        muacmap.put(days,m);
                    }

                }
                break;

            case 2:
                for (Event e : events) {

                    TrackedEntityDataValue weight = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(weightDataElement.uid()).one().blockingGet();
                    String w = weight.value();
                    if (w != null) {
                        Date d = e.eventDate();

                        long diff = d.getTime() - incidentDate.getTime();
                        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        intToDate.put(days,d);
                        rutfmap.put(days,Float.parseFloat(w));
                        entries.add(new Entry(days, Float.parseFloat(w)));
                    }
                    TrackedEntityDataValue muac = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(muacDataElement.uid()).one().blockingGet();
                    String m = muac.value();
                    if (m != null) {
                        Date d = e.eventDate();

                        long diff = d.getTime() - incidentDate.getTime();
                        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        muacmap.put(days,m);
                    }

                }
                break;
            case 3:
                for (Event e : events) {

                    TrackedEntityDataValue height = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(heightDataElement.uid()).one().blockingGet();
                    String h = height.value();

                    TrackedEntityDataValue weight = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(weightDataElement.uid()).one().blockingGet();
                    String w = weight.value();
                    if (h != null && w != null) {
                        intToDateWFH.put(Float.parseFloat(h), e.eventDate());
                        rutfmapWFH.put(Float.parseFloat(h),Float.parseFloat(w));
                        entries.add(new Entry(Float.parseFloat(h), Float.parseFloat(w)));
                    }
                    TrackedEntityDataValue muac = d2.trackedEntityModule().trackedEntityDataValues().byEvent().eq(e.uid()).byDataElement().in(muacDataElement.uid()).one().blockingGet();
                    String m = muac.value();
                    if (m != null) {
                        Date d = e.eventDate();

                        long diff = d.getTime() - incidentDate.getTime();
                        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        muacmapWFH.put(Float.parseFloat(h),m);
                    }

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
