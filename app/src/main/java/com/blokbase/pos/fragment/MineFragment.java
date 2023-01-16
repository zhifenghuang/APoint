package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.R;
import com.blokbase.pos.activity.AccountListActivity;
import com.blokbase.pos.activity.AddressListActivity;
import com.blokbase.pos.activity.ContactServiceActivity;
import com.blokbase.pos.activity.DataPreviewActivity;
import com.blokbase.pos.activity.IncomeRecordActivity;
import com.blokbase.pos.activity.InviteActivity;
import com.blokbase.pos.activity.InviteRecordActivity;
import com.blokbase.pos.activity.LoginActivity;
import com.blokbase.pos.activity.NoticeListActivity;
import com.blokbase.pos.activity.ProxyApplyActivity;
import com.blokbase.pos.activity.ProxyDetailActivity;
import com.blokbase.pos.activity.SecurityActivity;
import com.blokbase.pos.contract.MineContract;
import com.blokbase.pos.presenter.MinePresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.UserBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.dialog.AppUpgradeDialog;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;

import java.util.ArrayList;

public class MineFragment extends BaseFragment<MineContract.Presenter> implements MineContract.View {

    private AppUpgradeDialog mAppUpgradeDialog;
    private boolean mIsActivityPause;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.tvUID, R.id.tvNotification, R.id.ivSetting, R.id.tvIncomeRecord,
                R.id.tvProxyApply, R.id.tvInviteFriend, R.id.tvStatic, R.id.tvAPointAddress,
                R.id.tvRecommendRecord, R.id.tvAddressCenter, R.id.tvSwitch,
                R.id.tvLogout, R.id.tvContactService);
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
                BaseUtils.StaticParams.copyData(getActivity(), myInfo.getUserId());
                break;
            case R.id.tvAPointAddress:
                ArrayList<AssetsBean> list = DataManager.Companion.getInstance().getAssets();
                if (list.isEmpty()) {
                    return;
                }
                for (AssetsBean bean : list) {
                    if (bean.getSymbol().equalsIgnoreCase("INTEGRAL")) {
                        showToast(R.string.app_copy_success);
                        BaseUtils.StaticParams.copyData(getActivity(), bean.getAddress());
                        break;
                    }
                }
                break;
            case R.id.tvNotification:
                openActivity(NoticeListActivity.class);
                break;
            case R.id.tvIncomeRecord:
                openActivity(IncomeRecordActivity.class);
                break;
            case R.id.ivSetting:
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
            case R.id.tvStatic:
                openActivity(DataPreviewActivity.class);
                break;
            case R.id.tvContactService:
                openActivity(ContactServiceActivity.class);
                break;
            case R.id.tvLogout:
                DataManager.Companion.getInstance().logout();
                ((BaseActivity) getActivity()).finishAllActivity();
                openActivity(LoginActivity.class);
                break;
            case R.id.tvSwitch:
                openActivity(AccountListActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh() {
        if (getView() == null) {
            return;
        }
        getPresenter().getUserInfo();
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
            showToast(R.string.app_current_is_newest_version);
        } else if (!mIsActivityPause) {
            mAppUpgradeDialog = new AppUpgradeDialog(getActivity(), bean);
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
        ArrayList<AssetsBean> list = DataManager.Companion.getInstance().getAssets();
        if (list.isEmpty()) {
            return;
        }
        for (AssetsBean bean : list) {
            if (bean.getSymbol().equalsIgnoreCase("INTEGRAL")) {
                setText(R.id.tvAPointAddress, getString(R.string.app_a_points_address) + bean.getAddress());
                break;
            }
        }
    }
}
