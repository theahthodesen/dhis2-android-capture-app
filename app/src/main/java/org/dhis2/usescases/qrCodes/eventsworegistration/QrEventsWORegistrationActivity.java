package org.dhis2.usescases.qrCodes.eventsworegistration;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;

import org.dhis2.App;
import org.dhis2.R;
import org.dhis2.data.qr.QRCodeGenerator;
import org.dhis2.databinding.ActivityQrEventsWoRegistrationCodesBinding;
import org.dhis2.usescases.general.ActivityGlobalAbstract;
import org.dhis2.usescases.qrCodes.QrAdapter;
import org.dhis2.usescases.qrCodes.QrViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static org.dhis2.data.qr.QRjson.DATA_JSON_WO_REGISTRATION;
import static org.dhis2.data.qr.QRjson.EVENT_JSON;

/**
 * QUADRAM. Created by ppajuelo on 21/06/2018.
 */

public class QrEventsWORegistrationActivity extends ActivityGlobalAbstract implements QrEventsWORegistrationContracts.View {

    @Inject
    public QrEventsWORegistrationContracts.Presenter presenter;

    private ActivityQrEventsWoRegistrationCodesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((App) getApplicationContext()).userComponent().plus(new QrEventsWORegistrationModule()).inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_events_wo_registration_codes);
        binding.setName(getString(R.string.share_qr));
        binding.setPresenter(presenter);
        String eventUid = getIntent().getStringExtra("EVENT_UID");
        presenter.generateQrs(eventUid, this);
    }

    @Override
    public void showQR(@NonNull String value) {
        binding.setTitle(getString(R.string.qr_id));
        Glide.with(this).load(QRCodeGenerator.transform("", value)).into(binding.qrCode);
    }

    @Override
    public void onBackClick() {
        super.onBackPressed();
    }

}
