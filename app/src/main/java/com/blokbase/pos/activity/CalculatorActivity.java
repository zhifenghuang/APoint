package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.CalculatorContract;
import com.blokbase.pos.presenter.CalculatorPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.HomeDataBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.manager.DataManager;

import java.math.BigDecimal;

public class CalculatorActivity extends BaseActivity<CalculatorContract.Presenter> implements CalculatorContract.View {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_calculator;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_earnings_calculator);
        setText(R.id.tvPrice, DataManager.Companion.getInstance().getUtgPrice());
        HomeDataBean bean = DataManager.Companion.getInstance().getHomePosData();
        setText(R.id.tvTotalPledgeNum, Utils.removeZero(bean.getUltronNode()));
        getMetaSuccess(DataManager.Companion.getInstance().getAppMeta());
        getPresenter().appMeta();
        final EditText etPledgeNum = findViewById(R.id.etPledgeNum);
        etPledgeNum.requestFocus();
        etPledgeNum.setFocusable(true);
        etPledgeNum.setFocusableInTouchMode(true);

        etPledgeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = etPledgeNum.getText().toString().trim();
                if (text.contains(".")) {
                    String[] texts = text.split("\\.");
                    if (texts.length > 1) {
                        String str = texts[1];
                        if (str.length() > 4) {
                            setText(etPledgeNum, text.substring(0, texts[0].length() + 5));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = getTextById(R.id.etPledgeNum);
                calculate(TextUtils.isEmpty(text) ? null : new BigDecimal(text));
            }
        });
    }

    @NonNull
    @Override
    protected CalculatorContract.Presenter onCreatePresenter() {
        return new CalculatorPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getMetaSuccess(MetaBean bean) {
        if (isFinish() || bean == null || bean.getPool() == null) {
            return;
        }
        setText(R.id.tvPPLns, Utils.removeZero(BigDecimal.ONE.subtract(new BigDecimal(bean.getPool().getStaticRate())).multiply(new BigDecimal("100")).toString()));
        calculate(BigDecimal.ONE);
    }

    private void calculate(BigDecimal pledgeAmount) {
        if (pledgeAmount == null) {
            setText(R.id.tvDayIncome, "0");
            setText(R.id.tvToUsdt, "≈ $0");
            return;
        }
        MetaBean bean = DataManager.Companion.getInstance().getAppMeta();
        BigDecimal rate = new BigDecimal(bean.getPool().getStaticRate());
        try {
            BigDecimal totalProfit = new BigDecimal(bean.getPool().getTotalProfit());
            HomeDataBean homeData = DataManager.Companion.getInstance().getHomePosData();
            BigDecimal profit = totalProfit.multiply(rate).multiply(pledgeAmount).divide(new BigDecimal(homeData.getUltronNode()), 4, BigDecimal.ROUND_HALF_UP);
            setText(R.id.tvDayIncome, Utils.removeZero(profit.toString()));
            String toUsdt = profit.toString();
            if (!profit.equals(BigDecimal.ZERO)) {
                String price = DataManager.Companion.getInstance().getUtgPrice();
                toUsdt = profit.multiply(new BigDecimal(price)).toString();
            }
            setText(R.id.tvToUsdt, "≈ $" + Utils.removeZero(toUsdt));
        } catch (Exception e) {
            setText(R.id.tvDayIncome, "0");
            setText(R.id.tvToUsdt, "≈ $0");
        }
    }
}
