package org.dhis2.usescases.qrCodes;

import androidx.databinding.DataBindingUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;

import org.dhis2.App;
import org.dhis2.R;
import org.dhis2.data.qr.QRCodeGenerator;
import org.dhis2.databinding.ActivityQrCodesBinding;
import org.dhis2.usescases.general.ActivityGlobalAbstract;
import org.hisp.dhis.android.core.D2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static org.dhis2.data.qr.QRjson.ATTR_JSON;
import static org.dhis2.data.qr.QRjson.DATA_JSON;
import static org.dhis2.data.qr.QRjson.DATA_JSON_WO_REGISTRATION;
import static org.dhis2.data.qr.QRjson.ENROLLMENT_JSON;
import static org.dhis2.data.qr.QRjson.EVENTS_JSON;
import static org.dhis2.data.qr.QRjson.EVENT_JSON;
import static org.dhis2.data.qr.QRjson.RELATIONSHIP_JSON;
import static org.dhis2.data.qr.QRjson.TEI_JSON;

/**
 * QUADRAM. Created by ppajuelo on 21/06/2018.
 */

public class QrActivity extends ActivityGlobalAbstract implements QrContracts.View {

    @Inject
    public QrContracts.Presenter presenter;

    private ActivityQrCodesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((App) getApplicationContext()).userComponent().plus(new QrModule()).inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_codes);
        binding.setName(getString(R.string.share_qr));
        binding.setPresenter(presenter);
        String teiUid = getIntent().getStringExtra("TEI_UID");
        presenter.generateQrs(teiUid, this);
    }

    @Override
    protected void onStop() {
        presenter.onDetach();
        super.onStop();
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


    @Override
    public void showQRBitmap(Bitmap bitmap) {
        Glide.with(this).load(bitmap).into(binding.qrCode);
    }
}
