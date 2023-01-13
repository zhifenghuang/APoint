package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.WalletExchangeContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.WalletExchangePresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.ExchangeTipsBean;
import com.common.lib.bean.QuotationsBean;
import com.common.lib.constant.Constants;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class WalletExchangeActivity extends BaseActivity<WalletExchangeContract.Presenter> implements WalletExchangeContract.View {

    //   private int mType;
    private BigDecimal mRate, mExtraRate;
    private String mBalance;
    private String mFromSymbol, mToSymbol;
    private int mFromSymbolIcon, mToSymbolIcon;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_exchange;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_swap);
        setViewsOnClickListener(R.id.tvSureExchange);

        int exchangeWay = getIntent().getExtras().getInt(Constants.BUNDLE_EXTRA);
        if (exchangeWay == 0) {
            mFromSymbol = getString(R.string.app_uaa);
            mToSymbol = getString(R.string.app_a_points);
            mFromSymbolIcon = R.drawable.app_assets_uaa;
            mToSymbolIcon = R.drawable.app_assets_integral;
        } else if (exchangeWay == 1) {
            mFromSymbol = getString(R.string.app_usdt);
            mToSymbol = getString(R.string.app_a_points);
            mFromSymbolIcon = R.drawable.app_assets_usdt;
            mToSymbolIcon = R.drawable.app_assets_integral;
        } else {
            mFromSymbol = getString(R.string.app_a_points);
            mToSymbol = getString(R.string.app_usdt);
            mFromSymbolIcon = R.drawable.app_assets_integral;
            mToSymbolIcon = R.drawable.app_assets_usdt;
        }
        setText(R.id.tvFromSymbol, mFromSymbol);
        setImage(R.id.ivFromSymbol, mFromSymbolIcon);
        setText(R.id.tvToSymbol, mToSymbol);
        setImage(R.id.ivToSymbol, mToSymbolIcon);
        setText(R.id.tvBalance, getString(R.string.app_can_use_xxx_xxx, mFromSymbol, "0"));

        getPresenter().assetsList();
        initEditText();
        getPresenter().exchangeInfo(getNewString(mFromSymbol), getNewString(mToSymbol));
    }

    @NonNull
    @Override
    protected WalletExchangeContract.Presenter onCreatePresenter() {
        return new WalletExchangePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSwitch:
//                mType = (mType == 0 ? 1 : 0);
//                if (mType == 0) {
//                    setText(R.id.tvFromSymbol, R.string.app_uaa);
//                    setImage(R.id.ivFromSymbol, R.drawable.app_assets_uaa);
//                    setText(R.id.tvToSymbol, R.string.app_a_points);
//                    setImage(R.id.ivToSymbol, R.drawable.app_assets_integral);
//                    setText(R.id.tvBalance, getString(R.string.app_can_use_xxx_xxx, "UAA", mUAABalance));
//                } else {
//                    setText(R.id.tvFromSymbol, R.string.app_a_points);
//                    setImage(R.id.ivFromSymbol, R.drawable.app_assets_integral);
//                    setText(R.id.tvToSymbol, R.string.app_uaa);
//                    setImage(R.id.ivToSymbol, R.drawable.app_assets_uaa);
//                    setText(R.id.tvBalance, getString(R.string.app_can_use_xxx_xxx, getString(R.string.app_a_points), mAPointBalance));
//                }
//                calculate();
                break;
            case R.id.tvSureExchange:
                new InputDialog(this, new InputDialog.OnInputListener() {

                    @Override
                    public void checkInput(String psw) {
                        getPresenter().exchange(psw, getNewString(mFromSymbol), getNewString(mToSymbol), getTextById(R.id.etFromValue));
                    }
                });
                break;
        }
    }

    @Override
    public void getTickerSuccess(QuotationsBean bean) {

    }

    @Override
    public void exchangeSuccess() {
        showToast(R.string.app_exchange_success);
        setText(R.id.etFromValue, "");
        getPresenter().assetsList();
    }

    @Override
    public void getAssetsListSuccess(ArrayList<AssetsBean> list) {
        for (AssetsBean bean : list) {
            if (bean.getSymbol().equalsIgnoreCase(getNewString(mFromSymbol))) {
                mBalance = Utils.removeZero(bean.getBalance());
                break;
            }
        }
        setText(R.id.tvBalance, getString(R.string.app_can_use_xxx_xxx, mFromSymbol, mBalance));
    }

    private String getNewString(String value) {
        if (value.equals(getString(R.string.app_a_points))) {
            return "INTEGRAL";
        }
        return value;
    }

    @Override
    public void getExchangeInfoSuccess(ExchangeTipsBean bean) {
        if (bean == null) {
            return;
        }
        mExtraRate = new BigDecimal(bean.getRate());
        setText(R.id.tvTips, bean.getTipsStr());
        mRate = new BigDecimal("1");
        //setText(R.id.tvRate, "1" + mFromSymbol + " : 1" + mToSymbol);
    }


    private void calculate() {
        String text = getTextById(R.id.etFromValue);
        if (!TextUtils.isEmpty(text)) {
            String toValue = "0.00";
            try {
                if (mRate == null) {
                    getPresenter().exchangeInfo(getNewString(mFromSymbol), getNewString(mToSymbol));
                } else {
                    BigDecimal rate = mRate;
                    if (mExtraRate != null) {
                        rate = mRate.multiply(BigDecimal.ONE.subtract(mExtraRate));
                    }
                    toValue = new BigDecimal(text).multiply(rate).toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setText(R.id.tvToValue, Utils.removeZero(toValue));
        } else {
            setText(R.id.tvToValue, "");
        }
    }

    private void initEditText() {
        EditText etFromValue = findViewById(R.id.etFromValue);
        etFromValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        final TextView tvSureExchange = findViewById(R.id.tvSureExchange);
        tvSureExchange.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
        tvSureExchange.setEnabled(false);

        RxTextView.textChanges(etFromValue).skip(1).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (!TextUtils.isEmpty(getTextById(R.id.etFromValue))) {
                    tvSureExchange.setBackgroundResource(R.drawable.shape_common_btn_8);
                    tvSureExchange.setEnabled(true);
                } else {
                    tvSureExchange.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
                    tvSureExchange.setEnabled(false);
                }
            }
        });
    }
}
