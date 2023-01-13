package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.R;
import com.blokbase.pos.contract.MineContract;
import com.blokbase.pos.presenter.MinePresenter;
import com.blokbase.pos.util.Utils;
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
        setViewsOnClickListener(R.id.tvUID, R.id.tvNotification, R.id.tvEditProfile, R.id.tvIncomeRecord,
                R.id.tvProxyApply, R.id.tvInviteFriend,
                R.id.tvRecommendRecord, R.id.tvAddressCenter, R.id.tvSwitch,
                R.id.tvLogout, R.id.tvContactService);
        getUserInfoSuccess();
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
            case R.id.tvNotification:
                openActivity(NoticeListActivity.class);
                break;
            case R.id.tvIncomeRecord:
                openActivity(IncomeRecordActivity.class);
                break;
            case R.id.tvEditProfile:
                openActivity(SecurityActivity.class);
                break;
            case R.id.tvProxyApply:
                myInfo = DataManager.Companion.getInstance().getMyInfo();
                openActivity(myInfo.getAgentId() == 0 ? ProxyApplyActivity.class : ProxyDetailActivity.class);
                break;
            case R.id.tvInviteFriend:
                openActivity(InviteActivity.class);
                break;
            case R.id.tvRecommendRecord:
                openActivity(InviteRecordActivity.class);
                break;
            case R.id.tvAddressCenter:
                openActivity(AddressListActivity.class);
                break;
            case R.id.tvContactService:
                openActivity(ContactServiceActivity.class);
                break;
            case R.id.tvLogout:
                DataManager.Companion.getInstance().logout();
                finishAllActivity();
                openActivity(LoginActivity.class);
                break;
            case R.id.tvSwitch:
                openActivity(AccountListActivity.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPause = false;
        getPresenter().getUserInfo();
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
        setText(R.id.tvProxyApply, myInfo.getAgentId() == 0 ? R.string.app_proxy_apply : R.string.app_proxy_detail);
        //0 普通会员
        //10 经销商
        //20 平台商
        //30 区代理
        //40 市代理
        //50 省代理
        if (myInfo.getAgent() != null) {
            setText(R.id.tvProxy, myInfo.getAgent().getNameStr());
        }
        if (myInfo.getGrade() != null) {
            setText(R.id.tvLevel, myInfo.getGrade().getNameStr());
        }
    }


}
