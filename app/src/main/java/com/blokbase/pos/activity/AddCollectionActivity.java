package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.AddCollectionContract;
import com.blokbase.pos.presenter.AddCollectionPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.constant.EventBusEvent;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class AddCollectionActivity extends BaseActivity<AddCollectionContract.Presenter> implements AddCollectionContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_collection;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_add_collect);
        setViewsOnClickListener(R.id.tvOk);
        final TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
        tvOk.setTextColor(ContextCompat.getColor(this, R.color.text_color_4));
        tvOk.setEnabled(false);
        Observable.combineLatest(RxTextView.textChanges(findViewById(R.id.etObserverLink)).skip(1),
                RxTextView.textChanges(findViewById(R.id.etObserverRemark)).skip(1),
                (BiFunction<CharSequence, CharSequence, Boolean>) (charSequence, charSequence2) -> {
                    return !TextUtils.isEmpty(getTextById(R.id.etObserverLink))
                            && !TextUtils.isEmpty(getTextById(R.id.etObserverRemark));
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvOk.setBackgroundResource(R.drawable.shape_6961f3_9);
                    tvOk.setTextColor(ContextCompat.getColor(AddCollectionActivity.this, R.color.text_color_3));
                    tvOk.setEnabled(true);
                } else {
                    tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
                    tvOk.setTextColor(ContextCompat.getColor(AddCollectionActivity.this, R.color.text_color_4));
                    tvOk.setEnabled(false);
                }
            }
        });
    }

    @NonNull
    @Override
    protected AddCollectionContract.Presenter onCreatePresenter() {
        return new AddCollectionPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOk:
                getPresenter().addCollection("COLLECT",
                        getTextById(R.id.etObserverRemark), getTextById(R.id.etObserverLink));
                break;
        }
    }


    @Override
    public void addCollectionSuccess() {
        HashMap<String, String> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_OBSERVER, "COLLECT");
        EventBus.getDefault().post(map);
        finish();
    }
}
