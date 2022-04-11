package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.R;
import com.blokbase.pos.contract.MineContract;
import com.blokbase.pos.presenter.MinePresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.GradeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.dialog.AppUpgradeDialog;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;

public class MineActivity extends BaseActivity<MineContract.Presenter> implements MineContract.View {

    private AppUpgradeDialog mAppUpgradeDialog;
    private boolean mIsActivityPause;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_user_center);
        setViewsOnClickListener(R.id.tvUID, R.id.tvInviteFriend, R.id.tvSecurity, R.id.tvLanguage,
                R.id.tvService, R.id.tvNotice, R.id.tvAbout,
                R.id.tvVersion, R.id.tvLogout, R.id.llVersion);
        getUserInfoSuccess();
        getPresenter().getUserInfo();
    }

    @NonNull
    @Override
    protected MineContract.Presenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUID:
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, myInfo.getUserId());
                break;
            case R.id.tvInviteFriend:
                openActivity(InviteActivity.class);
                break;
            case R.id.tvSecurity:
                openActivity(SecurityActivity.class);
                break;
            case R.id.tvNotice:
                openActivity(NoticeListActivity.class);
                break;
            case R.id.tvService:
                //      openActivity(ServiceHelpActivity.class);
                openActivity(FAQActivity.class);
                break;
            case R.id.tvAbout:
                openActivity(AboutUsActivity.class);
                break;
            case R.id.tvLanguage:
                openActivity(LanguageActivity.class);
                break;
            case R.id.tvLogout:
                DataManager.Companion.getInstance().logout();
                finishAllActivity();
                openActivity(LoginActivity.class);
                break;
            case R.id.llVersion:
                getPresenter().checkVersion();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPause = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAppUpgradeDialog != null) {
            mAppUpgradeDialog.dismiss();
        }
        mAppUpgradeDialog = null;
    }

    @Override
    public void checkVersionSuccess(VersionBean bean) {
        if (bean.getVersionCode() <= BuildConfig.VERSION_CODE) {
            showToast(R.string.app_current_is_newest_verision);
        } else if (!mIsActivityPause) {
            mAppUpgradeDialog = new AppUpgradeDialog(this, bean);
            mAppUpgradeDialog.show();
        }
    }

    @Override
    public void getUserInfoSuccess() {
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tvUID, "UID: " + myInfo.getUserId());
        setText(R.id.tvName, myInfo.getLoginAccount());
        setText(R.id.tvVersion, BuildConfig.VERSION_NAME);
        GradeBean grade = myInfo.getGrade();
        if (grade == null) {
            return;
        }
        setText(R.id.tvGrade, grade.getNameStr());
        int drawableId = getResources().getIdentifier("app_node_" + grade.getId(), "drawable", getPackageName());
        setImage(R.id.ivGrade, drawableId);
    }
}
