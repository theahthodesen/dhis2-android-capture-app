package org.dhis2.usescases.qrCodes;

import android.annotation.SuppressLint;

import org.dhis2.data.qr.QRInterface;
import org.hisp.dhis.android.core.D2;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class QrPresenter implements QrContracts.Presenter {

    private final QRInterface qrInterface;
    private QrContracts.View view;
    private CompositeDisposable disposable;
    private D2 d2;

    QrPresenter(QRInterface qrInterface, D2 d2) {
        this.qrInterface = qrInterface;
        this.d2 = d2;
        this.disposable = new CompositeDisposable();
    }

    @SuppressLint({"RxLeakedSubscription", "CheckResult"})
    public void generateQrs(@NonNull String enrollmentUid, @NonNull QrContracts.View view) {
        this.view = view;
        disposable.add(d2.smsModule().qrCodeCase().generateEnrollmentCode(enrollmentUid)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showQR,
                        Timber::d
                )
        );

        /*disposable.add(qrInterface.getUncodedData(teUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::showQRBitmap,
                        Timber::e
                )
        );*/
    }

    @Override
    public void onBackClick() {
        if (view != null)
            view.onBackClick();
    }


    @Override
    public void onDetach() {
        disposable.clear();
    }

}
