package com.blokbase.pos.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.WalletTransferContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.WalletTransferPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.activity.CaptureActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.TransferFeeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.PermissionUtil;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class WalletTransferActivity extends BaseActivity<WalletTransferContract.Presenter> implements WalletTransferContract.View {

    private AssetsBean mSelectAssets;
    private BigDecimal mFeeRate;
    private String mMin;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_transfer;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.ivScan, R.id.tvAll, R.id.tvWithdraw);
        mSelectAssets = (AssetsBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvTitle, mSelectAssets.getSymbol());
        setText(R.id.tvBalance, getString(R.string.app_withdrawable_balance, Utils.removeZero(mSelectAssets.getBalance())) + " " + mSelectAssets.getSymbol());
        initEditText();
        getPresenter().transferInfo(mSelectAssets.getSymbol());
    }

    @NonNull
    @Override
    protected WalletTransferContract.Presenter onCreatePresenter() {
        return new WalletTransferPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivScan:
                if (!PermissionUtil.INSTANCE.isGrantPermission(this, Manifest.permission.CAMERA)) {
                    requestPermission(null, Manifest.permission.CAMERA);
                    return;
                }
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                        1001
                );
                openActivity(CaptureActivity.class);
                break;
            case R.id.tvAll:
                setText(R.id.etNums, mSelectAssets.getBalance());
                break;
            case R.id.tvWithdraw:
                String amount = getTextById(R.id.etNums);
                try {
                    if (!TextUtils.isEmpty(mMin) && Double.parseDouble(amount) < Double.parseDouble(mMin)) {
                        showToast(getString(R.string.app_transfer_min_xxx, mMin));
                        return;
                    }
                } catch (Exception e) {
                }
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                if (!myInfo.getPaymentStatus()) {
                    showNoPayPswDialog();
                    return;
                }
                new InputDialog(this, new InputDialog.OnInputListener() {

                    @Override
                    public void checkInput(String input) {
                        getPresenter().transfer(input, mSelectAssets.getSymbol(),
                                getTextById(R.id.etNums),
                                getTextById(R.id.etAddress));
                    }
                }, getString(R.string.app_sure_withdraw));
                break;
        }
    }

    private void initEditText() {
        EditText etNums = findViewById(R.id.etNums);
        etNums.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etNums.getText().toString();
                if (text.length() > 1 && text.startsWith("0") && text.charAt(1) != '.') {
                    text = text.substring(1);
                    etNums.setText(text);
                    return;
                }
                if (text.contains(".")) {
                    String[] texts = text.split("\\.");
                    if (texts.length > 1) {
                        String str = texts[1];
                        if (str.length() > 4) {
                            text = text.substring(0, texts[0].length() + 5);
                            setText(R.id.etNums, text);
                            resetFee(getTextById(R.id.etNums));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                resetFee(getTextById(R.id.etNums));
            }
        });
        EditText etAddress = findViewById(R.id.etAddress);
        final TextView tvTransfer = findViewById(R.id.tvWithdraw);
        tvTransfer.setBackgroundResource(R.drawable.shape_ededed_9);
        tvTransfer.setTextColor(ContextCompat.getColor(WalletTransferActivity.this, R.color.text_color_4));
        tvTransfer.setEnabled(false);
        Observable.combineLatest(RxTextView.textChanges(etNums).skip(1),
                RxTextView.textChanges(etAddress).skip(1),
                (BiFunction<CharSequence, CharSequence, Boolean>) (charSequence, charSequence2) -> {
                    return !TextUtils.isEmpty(getTextById(R.id.etAddress)) && !TextUtils.isEmpty(getTextById(R.id.etNums));
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvTransfer.setBackgroundResource(R.drawable.shape_6961f3_9);
                    tvTransfer.setTextColor(ContextCompat.getColor(WalletTransferActivity.this, R.color.text_color_3));
                    tvTransfer.setEnabled(true);
                } else {
                    tvTransfer.setBackgroundResource(R.drawable.shape_ededed_9);
                    tvTransfer.setTextColor(ContextCompat.getColor(WalletTransferActivity.this, R.color.text_color_4));
                    tvTransfer.setEnabled(false);
                }
            }
        });
    }

    private void resetFee(String text) {
        String fee = "0.00";
        if (!TextUtils.isEmpty(text)) {
            try {
                if (mFeeRate == null) {
                    getPresenter().transferInfo(mSelectAssets.getSymbol());
                } else {
                    fee = new BigDecimal(text).multiply(mFeeRate).toPlainString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setText(R.id.tvFee, Utils.removeZero(fee) + " UTG");
        setText(R.id.tvActual, Utils.removeZero(text) + " UTG");
    }

    @Override
    public void transferSuccess() {
        showToast(R.string.app_widthdraw_success);
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_ASSETS, "");
        EventBus.getDefault().post(map);
        finish();
    }

    @Override
    public void transferFailed() {
        showToast(R.string.app_withdraw_failed);
    }

    @Override
    public void getTransferInfoSuccess(TransferFeeBean bean) {
        mFeeRate = new BigDecimal(bean.getFeeRate());
        mMin = bean.getMin();
        setText(R.id.tvTip, bean.getTipsStr());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (map == null) {
            return;
        }
        if (map.containsKey(EventBusEvent.SCAN_RESULT)) {
            String address = (String) map.get(EventBusEvent.SCAN_RESULT);
            if (!TextUtils.isEmpty(address)) {
                setText(R.id.etAddress, address);
            }
        }
    }

    private void showNoPayPswDialog() {
        showTwoBtnDialog(getString(R.string.app_not_set_pay_password_go_set), getString(R.string.app_cancel), getString(R.string.app_ok),
                new OnClickCallback() {
                    @Override
                    public void onClick(int viewId) {
                        if (viewId == R.id.btn2) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.BUNDLE_EXTRA, DataManager.Companion.getInstance().getMyInfo());
                            bundle.putInt(Constants.BUNDLE_EXTRA_2, 3);
                            openActivity(SetPasswordActivity.class, bundle);
                        }
                    }
                });
    }
}
