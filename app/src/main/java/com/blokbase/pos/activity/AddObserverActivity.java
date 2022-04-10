package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.AddObserverContract;
import com.blokbase.pos.presenter.AddObserverPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ObserverBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import io.reactivex.functions.Consumer;

public class AddObserverActivity extends BaseActivity<AddObserverContract.Presenter> implements AddObserverContract.View {

    private boolean isIncomeChoose, isAccountChoose;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_observer;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_add_observer);
        setViewsOnClickListener(R.id.llPermission1, R.id.llPermission2, R.id.tvOk);
        isIncomeChoose = false;
        isAccountChoose = false;
        final TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
        tvOk.setTextColor(ContextCompat.getColor(AddObserverActivity.this, R.color.text_color_4));
        tvOk.setEnabled(false);
        RxTextView.textChanges(findViewById(R.id.etObserverRemark)).skip(1).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (TextUtils.isEmpty(getTextById(R.id.etObserverRemark))) {
                    tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
                    tvOk.setTextColor(ContextCompat.getColor(AddObserverActivity.this, R.color.text_color_4));
                    tvOk.setEnabled(false);
                } else {
                    tvOk.setBackgroundResource(R.drawable.shape_6961f3_9);
                    tvOk.setTextColor(ContextCompat.getColor(AddObserverActivity.this, R.color.text_color_3));
                    tvOk.setEnabled(true);
                }
            }
        });
    }

    @NonNull
    @Override
    protected AddObserverContract.Presenter onCreatePresenter() {
        return new AddObserverPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPermission1:
                isIncomeChoose = !isIncomeChoose;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).
                        setImageResource(isIncomeChoose ? R.drawable.app_choose : R.drawable.app_not_choose);
                break;
            case R.id.llPermission2:
                isAccountChoose = !isAccountChoose;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).
                        setImageResource(isAccountChoose ? R.drawable.app_choose : R.drawable.app_not_choose);
                break;
            case R.id.tvOk:
                HashMap<String, Boolean> map = new HashMap<>();
                map.put("income", isIncomeChoose);
                map.put("account", isAccountChoose);
                getPresenter().addObserver("SHARE", getTextById(R.id.etObserverRemark), map);
                break;
        }
    }


    @Override
    public void addObserverSuccess(ObserverBean bean) {
        HashMap<String, String> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_OBSERVER, "SHARE");
        EventBus.getDefault().post(map);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_EXTRA, bean);
        openActivity(ObserverLinkActivity.class, bundle);
        finish();
    }
}
