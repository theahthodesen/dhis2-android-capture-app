package org.dhis2.usescases.qrCodes.eventsworegistration;

import androidx.annotation.NonNull;

import org.dhis2.usescases.qrCodes.QrViewModel;

import java.util.List;

public class QrEventsWORegistrationContracts {

    public interface View {
        void showQR(@NonNull String value);

        void onBackClick();
    }

    public interface Presenter {
        void generateQrs(@NonNull String eventUid, @NonNull QrEventsWORegistrationContracts.View view);

        void onBackClick();
    }
}
