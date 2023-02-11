package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.UpdateNickContract;
import com.blokbase.pos.presenter.UpdateNickPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.UserBean;
import com.common.lib.manager.DataManager;


public class UpdateNickActivity extends BaseActivity<UpdateNickContract.Presenter> implements UpdateNickContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_nick;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.topView);
        setText(R.id.tvTitle, R.string.app_nick);
        TextView tvRight = findViewById(R.id.tvRight);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.app_modify);
        tvRight.setOnClickListener(this);
        setText(R.id.etNick,DataManager.Companion.getInstance().getMyInfo().getNick());
    }

    @NonNull
    @Override
    protected UpdateNickContract.Presenter onCreatePresenter() {
        return new UpdateNickPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                String nick = getTextById(R.id.etNick);
                if (TextUtils.isEmpty(nick)) {
                    showToast(R.string.app_please_input_nick);
                    return;
                }
                getPresenter().updateNick(nick);
                break;
        }
    }

    @Override
    public void updateNickSuccess(String nick) {
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
        myInfo.setNickName(nick);
        DataManager.Companion.getInstance().saveMyInfo(myInfo);
        if (isFinish()) {
            return;
        }
        showToast(R.string.app_modify_success);
        finish();
    }
}
