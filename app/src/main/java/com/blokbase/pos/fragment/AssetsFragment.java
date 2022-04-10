package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.LockIncomeRecordActivity;
import com.blokbase.pos.activity.MineActivity;
import com.blokbase.pos.activity.WalletAddressActivity;
import com.blokbase.pos.activity.WalletDetailActivity;
import com.blokbase.pos.activity.WalletRecordActivity;
import com.blokbase.pos.activity.WalletTransferActivity;
import com.blokbase.pos.adapter.AssetsAdapter;
import com.blokbase.pos.contract.AssetsContract;
import com.blokbase.pos.presenter.AssetsPresenter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.IncomeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.PrefUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AssetsFragment extends BaseFragment<AssetsContract.Presenter> implements AssetsContract.View {

    private AssetsAdapter mAdapter;
    private AssetsBean mSelectAssets;

    @NonNull
    @Override
    protected AssetsContract.Presenter onCreatePresenter() {
        return new AssetsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_assets;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        setViewsOnClickListener(R.id.ivMore, R.id.ivProfile, R.id.ivShow,
                R.id.llAssets, R.id.llCharge, R.id.llWithdraw, R.id.tvLockRecord);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        showAssets();
        getPresenter().getIncomeInfo();
        getIncomeInfoSuccess(DataManager.Companion.getInstance().getIncome());
    }

    private AssetsAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new AssetsAdapter(getActivity());
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    openActivity(WalletDetailActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMore:
            case R.id.ivProfile:
                openActivity(MineActivity.class);
                break;
            case R.id.llAssets:
                openActivity(WalletRecordActivity.class);
                break;
            case R.id.ivShow:
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                boolean isShowAssets = PrefUtil.getBoolean(getActivity(),
                        myInfo.getLoginAccount(), true);
                isShowAssets = !isShowAssets;
                PrefUtil.putBoolean(getActivity(),
                        myInfo.getLoginAccount(), isShowAssets);
                showAssets();
                setImage(R.id.ivShow, isShowAssets ? R.drawable.app_show_assets : R.drawable.app_hide_assets);
                break;
            case R.id.llCharge:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, mSelectAssets);
                openActivity(WalletAddressActivity.class, bundle);
                break;
            case R.id.llWithdraw:
                bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, mSelectAssets);
                openActivity(WalletTransferActivity.class, bundle);
                break;
            case R.id.tvLockRecord:
                openActivity(LockIncomeRecordActivity.class);
                break;
        }
    }

    public void showAssets() {
        ArrayList<AssetsBean> list = DataManager.Companion.getInstance().getAssets();
        if (getView() == null || list.isEmpty()) {
            return;
        }
        getAdapter().getData().clear();
        for (AssetsBean bean : list) {
            if (bean.getSymbol().equalsIgnoreCase("utg")) {
                mSelectAssets = bean;
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                boolean isShowAssets = PrefUtil.getBoolean(getActivity(),
                        myInfo.getLoginAccount(), true);
                if (isShowAssets) {
                    setText(R.id.tvTotalBalance, Utils.removeZero(bean.getBalance()));
                    String toUsdt = bean.getBalance();
                    BigDecimal balance = new BigDecimal(Utils.removeZero(bean.getBalance()));
                    if (!balance.equals(BigDecimal.ZERO)) {
                        String price = DataManager.Companion.getInstance().getUtgPrice();
                        toUsdt = balance.multiply(new BigDecimal(price)).toString();
                    }
                    setText(R.id.tvTotalToUsdt, "≈ $" + Utils.removeZero(toUsdt));
                } else {
                    setText(R.id.tvTotalBalance, "******");
                    setText(R.id.tvTotalToUsdt, "≈ $******");
                }
                getAdapter().addData(bean);
                getAdapter().setShowAssets(isShowAssets);
                break;
            }
        }
    }

    @Override
    public void getIncomeInfoSuccess(IncomeBean bean) {
        if (bean == null) {
            return;
        }
        setText(R.id.tvTotalIncome, Utils.removeZero(bean.getTotal()));
        setText(R.id.tvDailyIncome, Utils.removeZero(bean.getToday()));
        setText(R.id.tvReleased, Utils.removeZero(bean.getReleased()));
        setText(R.id.tvTotalPerformance, Utils.removeZero(bean.getRightAmount()));
    }
}
