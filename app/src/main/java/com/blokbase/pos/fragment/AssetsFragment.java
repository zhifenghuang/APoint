package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.CheckInActivity;
import com.blokbase.pos.activity.WalletAddressActivity;
import com.blokbase.pos.activity.WalletDetailActivity;
import com.blokbase.pos.activity.WalletRecordActivity;
import com.blokbase.pos.activity.WalletTransferActivity;
import com.blokbase.pos.adapter.AssetsAdapter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.AssetsBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AssetsFragment extends BaseFragment<EmptyContract.Presenter> implements EmptyContract.View {

    private AssetsAdapter mAdapter;

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_assets;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.llAssets, R.id.llWithdraw, R.id.llCharge, R.id.ivMark);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        showAssets();
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
            case R.id.ivMark:
                openActivity(CheckInActivity.class);
                break;
            case R.id.llAssets:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_EXTRA, "UAA");
                openActivity(WalletRecordActivity.class, bundle);
                break;
            case R.id.llWithdraw:
                ArrayList<AssetsBean> list = DataManager.Companion.getInstance().getAssets();
                if (getView() == null || list.isEmpty()) {
                    return;
                }
                for (AssetsBean bean : list) {
                    String symbol = bean.getSymbol();
                    if (symbol.equalsIgnoreCase("uaa")) {
                        bundle = new Bundle();
                        bundle.putSerializable(Constants.BUNDLE_EXTRA, bean);
                        openActivity(WalletTransferActivity.class, bundle);
                    }
                }
                break;
            case R.id.llCharge:
                list = DataManager.Companion.getInstance().getAssets();
                if (getView() == null || list.isEmpty()) {
                    return;
                }
                for (AssetsBean bean : list) {
                    String symbol = bean.getSymbol();
                    if (symbol.equalsIgnoreCase("uaa")) {
                        bundle = new Bundle();
                        bundle.putSerializable(Constants.BUNDLE_EXTRA, bean);
                        openActivity(WalletAddressActivity.class, bundle);
                    }
                }
                break;
        }
    }

    public void showAssets() {
        ArrayList<AssetsBean> list = DataManager.Companion.getInstance().getAssets();
        if (getView() == null || list.isEmpty()) {
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); ) {
            AssetsBean bean = list.get(i);
            if (bean.getVisible() == 0) {
                list.remove(i);
                continue;
            }
            ++i;
            String symbol = bean.getSymbol();
            if (symbol.equalsIgnoreCase("usdt") ||
                    symbol.equalsIgnoreCase("uaa")) {
                total = total.add(new BigDecimal(bean.getBalance()));
            }
        }
        setText(R.id.tvTotalBalance, Utils.removeZero(total.toString()));
        setText(R.id.tvTotalToUsdt, "??? $" + Utils.removeZero(total.toString()));
        getAdapter().setNewInstance(list);
    }
}
