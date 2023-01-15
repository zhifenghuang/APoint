package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.InviteContract;
import com.blokbase.pos.presenter.InvitePresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.InviteBean;
import com.common.lib.bean.PosterBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.bean.UserBean;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;

import java.util.ArrayList;

public class InviteActivity extends BaseActivity<InviteContract.Presenter> implements InviteContract.View {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_invite_friend);
        setViewsOnClickListener(R.id.tvCopy);
        resetUI();
        getPresenter().poster();
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tvIdentity, getString(R.string.app_i_am) + myInfo.getGrade().getNameStr());
        setText(R.id.tvDes, myInfo.getGrade().getDesc());
        if (myInfo.getGradeId() == 0) {
            setTextColor(R.id.tvIdentity, R.color.text_color_2);
            setTextColor(R.id.tvDes, R.color.text_color_2);
            setText(R.id.tvTip, R.string.app_identity_no_buy_package);
        } else {
            setTextColor(R.id.tvIdentity, R.color.text_color_7);
            setTextColor(R.id.tvDes, R.color.text_color_7);
            setText(R.id.tvTip, R.string.app_current_progress);
            setText(R.id.tvProgress, (myInfo.getRefereeCount() > 2 ? 2 : myInfo.getRefereeCount()) + "/" + myInfo.getCompleteCount());
        }
    }


    @NonNull
    @Override
    protected InviteContract.Presenter onCreatePresenter() {
        return new InvitePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCopy:
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, getTextById(R.id.tvLink));
                break;
        }
    }

    private void resetUI() {
        PosterBean poster = DataManager.Companion.getInstance().getPoster();
        if (poster != null) {
            setText(R.id.tvLink, poster.getLink());
        }
    }


    @Override
    public void getPosterSuccess(PosterBean poster) {
        DataManager.Companion.getInstance().savePoster(poster);
        resetUI();
    }

    @Override
    public void getInviteDetailSuccess(int pageIndex, int totalCount, ArrayList<InviteBean> list) {

    }

    @Override
    public void getInviteDetailFailed() {

    }
}
