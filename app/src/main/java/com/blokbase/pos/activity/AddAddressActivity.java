package com.blokbase.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.AddAddressContract;
import com.blokbase.pos.presenter.AddAddressPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.DistrictBean;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

public class AddAddressActivity extends BaseActivity<AddAddressContract.Presenter>
        implements AddAddressContract.View {

    private DistrictBean mSelectP, mSelectC, mSelectD;
    private int mIsDefault;
    private ReceiveAddressBean mReceiveAddress;


    private ActivityResultLauncher mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                mSelectP = (DistrictBean) data.getSerializableExtra(Constants.BUNDLE_EXTRA);
                mSelectC = (DistrictBean) data.getSerializableExtra(Constants.BUNDLE_EXTRA_2);
                mSelectD = (DistrictBean) data.getSerializableExtra(Constants.BUNDLE_EXTRA_3);
                setText(R.id.tvCity, mSelectP.getText() + "，" + mSelectC.getText() + "，" + mSelectD.getText());
            }
        }
    });


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_receive_goods_address);
        mIsDefault = 0;
        setViewsOnClickListener(R.id.llSelectCity, R.id.tvOk, R.id.llDefault);
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt(Constants.BUNDLE_EXTRA, 0);
        if (type == 1) {
            mReceiveAddress = (ReceiveAddressBean) bundle.getSerializable(Constants.BUNDLE_EXTRA_2);
            setText(R.id.etReceiver, mReceiveAddress.getName());
            setText(R.id.etPhone, mReceiveAddress.getMobile());
            mSelectP = new DistrictBean();
            mSelectP.setValue(mReceiveAddress.getProvinceCode());
            mSelectP.setText(mReceiveAddress.getProvinceName());
            mSelectC = new DistrictBean();
            mSelectC.setValue(mReceiveAddress.getCityCode());
            mSelectC.setText(mReceiveAddress.getCityName());
            mSelectD = new DistrictBean();
            mSelectD.setValue(mReceiveAddress.getDistrictCode());
            mSelectD.setText(mReceiveAddress.getDistrictName());
            setText(R.id.tvCity, mSelectP.getText() + "，" + mSelectC.getText() + "，" + mSelectD.getText());
            setText(R.id.etDetail, mReceiveAddress.getDetail());
            mIsDefault = mReceiveAddress.getDefault();
            setImage(R.id.ivDefault, mIsDefault == 0 ? R.drawable.app_undefault : R.drawable.app_default);
        }
    }


    @NonNull
    @Override
    protected AddAddressContract.Presenter onCreatePresenter() {
        return new AddAddressPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llDefault:
                mIsDefault = mIsDefault == 0 ? 10 : 0;
                setImage(R.id.ivDefault, mIsDefault == 0 ? R.drawable.app_undefault : R.drawable.app_default);
                break;
            case R.id.llSelectCity:
                Intent intent = new Intent(this, SelectCityActivity.class);
                intent.putExtra(Constants.BUNDLE_EXTRA, 3);
                mLauncher.launch(intent);
                break;
            case R.id.tvOk:
                String name = getTextById(R.id.etReceiver);
                if (TextUtils.isEmpty(name)) {
                    showToast(R.string.app_input_receiver);
                    return;
                }
                String phone = getTextById(R.id.etPhone);
                if (TextUtils.isEmpty(name)) {
                    showToast(R.string.app_input_phone);
                    return;
                }
                if (mSelectP == null) {
                    showToast(R.string.app_select_city);
                    return;
                }
                String detail = getTextById(R.id.etDetail);
                if (TextUtils.isEmpty(detail)) {
                    showToast(R.string.app_street);
                    return;
                }
                if (mReceiveAddress != null) {
                    getPresenter().saveAddress(mReceiveAddress.getId(), name, phone, mSelectP.getValue(), mSelectP.getText(),
                            mSelectC.getValue(), mSelectC.getText(), mSelectD.getValue(), mSelectD.getText(),
                            detail, mIsDefault);
                } else {
                    getPresenter().addAddress(name, phone, mSelectP.getValue(), mSelectP.getText(),
                            mSelectC.getValue(), mSelectC.getText(), mSelectD.getValue(), mSelectD.getText(),
                            detail, mIsDefault);
                }

                break;
        }
    }

    @Override
    public void addAddressSuccess() {
        notifyList();
        showToast(R.string.app_add_success);
    }

    @Override
    public void saveAddressSuccess() {
        notifyList();
        showToast(R.string.app_modify_success);
    }

    private void notifyList() {
        HashMap<String, String> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_ADDRESS_LIST, "");
        EventBus.getDefault().post(map);
        finish();
    }
}
