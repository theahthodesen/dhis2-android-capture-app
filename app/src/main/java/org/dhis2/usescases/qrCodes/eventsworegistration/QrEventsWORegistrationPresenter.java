package org.dhis2.usescases.qrCodes.eventsworegistration;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import org.dhis2.data.qr.QRInterface;
import org.hisp.dhis.android.core.D2;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class QrEventsWORegistrationPresenter implements QrEventsWORegistrationContracts.Presenter {

    private final QRInterface qrInterface;
    private QrEventsWORegistrationContracts.View view;
    private D2 d2;

    QrEventsWORegistrationPresenter(QRInterface qrInterface, D2 d2) {
        this.qrInterface = qrInterface;
        this.d2 = d2;
    }

    @SuppressLint({"RxLeakedSubscription", "CheckResult"})
    public void generateQrs(@NonNull String eventUid, @NonNull QrEventsWORegistrationContracts.View view) {
        this.view = view;
        d2.smsModule().qrCodeCase().generateSimpleEventCode(eventUid)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showQR,
                        Timber::d
                );
    }

    @Override
    public void onBackClick() {
        if (view != null)
            view.onBackClick();
    }


}
