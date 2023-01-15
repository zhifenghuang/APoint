package com.blokbase.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.ProxyApplyContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.ProxyApplyPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AgentBean;
import com.common.lib.bean.DistrictBean;
import com.common.lib.constant.Constants;

import java.util.ArrayList;

public class ProxyApplyActivity extends BaseActivity<ProxyApplyContract.Presenter> implements ProxyApplyContract.View {

    private DistrictBean mSelectDistrict;
    private String mSelectDistrictValue;
    private AgentBean mSelectAgent;
    private int mCurrentSelect = 0;


    private ActivityResultLauncher mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                mSelectDistrict = (DistrictBean) data.getSerializableExtra(Constants.BUNDLE_EXTRA);
                mSelectDistrictValue = data.getStringExtra(Constants.BUNDLE_EXTRA_2);
                setText(R.id.tvSelect, mSelectDistrictValue);
                findViewById(R.id.tvApply).setBackgroundResource(R.drawable.shape_common_btn_8);
            }
        }
    });

    @Override
    protected int getLayoutId() {
        return R.layout.activity_proxy_apply;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_proxy_apply);
        mSelectDistrictValue = "";
        getPresenter().agentGoods();
        setViewsOnClickListener(R.id.tvSelect, R.id.tvApply);
        setTextViewLinearGradient(R.id.tvSelect);
    }

    @NonNull
    @Override
    protected ProxyApplyContract.Presenter onCreatePresenter() {
        return new ProxyApplyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelect:
                Intent intent = new Intent(this, SelectCityActivity.class);
                intent.putExtra(Constants.BUNDLE_EXTRA, mCurrentSelect);
                mLauncher.launch(intent);
                break;
            case R.id.tvApply:
                if (mSelectDistrict == null) {
                    return;
                }
                if (mSelectAgent == null) {
                    return;
                }
                new InputDialog(this, new InputDialog.OnInputListener() {

                    @Override
                    public void checkInput(String input) {
                        getPresenter().submitAgent(mSelectAgent.getId(), mSelectDistrict.getValue(), input, mSelectDistrictValue);
                    }
                }, getString(R.string.app_please_input_your_pay_psw));
                break;
        }
    }

    @Override
    public void getAgentGoodsSuccess(ArrayList<AgentBean> list) {
        if (isFinish()) {
            return;
        }
        setViewVisible(R.id.ll);
        if (list.size() > 0) {
            resetAgent(0, findViewById(R.id.ll1), list.get(0));
            setText(R.id.tvFee, list.get(0).getIntegral() + getString(R.string.app_a_points));
            mSelectAgent = list.get(0);
        }
        if (list.size() > 1) {
            resetAgent(1, findViewById(R.id.ll2), list.get(1));
        }
        if (list.size() > 2) {
            resetAgent(2, findViewById(R.id.ll3), list.get(2));
        }
        mCurrentSelect = 0;
        mSelectDistrict = null;
        findViewById(R.id.tvApply).setBackgroundResource(R.drawable.shape_common_disable_btn_8);
    }

    private void resetAgent(int pos, LinearLayout ll, AgentBean bean) {
        ll.setVisibility(View.VISIBLE);
        ll.setTag(bean);
        ll.setTag(R.id.tag, pos);
        ((TextView) ll.getChildAt(0)).setText(bean.getName());
        ((TextView) ll.getChildAt(1)).setText(bean.getIntegral() + getString(R.string.app_a_points));
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAgentUI((int) v.getTag(R.id.tag), (AgentBean) v.getTag());
            }
        });
    }

    private void resetAgentUI(int pos, AgentBean bean) {
        if (mCurrentSelect == pos) {
            return;
        }
        mSelectDistrict = null;
        mSelectDistrictValue = "";
        mSelectAgent = bean;
        findViewById(R.id.tvApply).setBackgroundResource(R.drawable.shape_common_disable_btn_8);
        setText(R.id.tvSelect, R.string.app_please_select);
        mCurrentSelect = pos;
        setText(R.id.tvFee, bean.getIntegral() + getString(R.string.app_a_points));
        for (int i = 0; i < 3; ++i) {
            LinearLayout l = findViewById(getResources().getIdentifier("ll" + (i + 1), "id", getPackageName()));
            if (i == pos) {
                l.setBackgroundResource(R.drawable.shape_common_btn_4);
                ((TextView) l.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.text_color_3));
                ((TextView) l.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.text_color_3));
            } else {
                l.setBackgroundResource(R.drawable.shape_stroke_e4e5e7_4);
                ((TextView) l.getChildAt(0)).setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
                ((TextView) l.getChildAt(1)).setTextColor(ContextCompat.getColor(this, R.color.text_color_2));
            }
        }
    }

    @Override
    public void submitAgentSuccess() {
        if (isFinish()) {
            return;
        }
        finish();
        showToast(R.string.app_apply_success);
    }
}
