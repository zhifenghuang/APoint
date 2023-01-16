package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.ExchangeWayAdapter;
import com.blokbase.pos.adapter.ExpressNOAdapter;
import com.blokbase.pos.adapter.TransferAdapter;
import com.blokbase.pos.contract.WalletDetailContract;
import com.blokbase.pos.presenter.WalletDetailPresenter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.ExpressNOBean;
import com.common.lib.bean.TransferBean;
import com.common.lib.constant.Constants;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;

import java.util.ArrayList;

public class WalletDetailActivity extends BaseActivity<WalletDetailContract.Presenter> implements WalletDetailContract.View {

    private AssetsBean mSelectAssets;
    private TransferAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.topView);
        setBackgroundColor(R.id.topView, R.color.color_bg_theme);
        TextView tvRight = findViewById(R.id.tvRight);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.app_swap);
        tvRight.setOnClickListener(this);

        mSelectAssets = (AssetsBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        int drawableId = 0;
        try {
            drawableId = getResources()
                    .getIdentifier("app_assets_" + mSelectAssets.getSymbol().toLowerCase(), "drawable",
                            getPackageName());
            setImage(R.id.iv, drawableId);
        } catch (Exception e) {
        }
        setViewsOnClickListener(R.id.tvMore,
                R.id.tvWithDraw, R.id.tvCharge);
        setText(R.id.tvTitle, mSelectAssets.getSymbol2());
        resetBalanceUI();
        setTextViewLinearGradient(R.id.tvRight, R.id.tvWithDraw);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
    }

    private TransferAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new TransferAdapter(this);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    openActivity(AssetsRecordDetailActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected WalletDetailContract.Presenter onCreatePresenter() {
        return new WalletDetailPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().assetsList();
        getPresenter().transferList(mSelectAssets.getSymbol(), 1, new ArrayList<>());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMore:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BUNDLE_EXTRA, mSelectAssets.getSymbol());
                openActivity(WalletRecordActivity.class, bundle);
                break;
            case R.id.tvRight:
                ArrayList<String> list = new ArrayList<>();
                list.add(getString(R.string.app_please_exchange_way_0));
                list.add(getString(R.string.app_please_exchange_way_1));
                list.add(getString(R.string.app_please_exchange_way_2));
                showExchangeWayList(list);
                break;
            case R.id.tvCharge:
                bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, mSelectAssets);
                openActivity(WalletAddressActivity.class, bundle);
                break;
            case R.id.tvWithDraw:
                bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, mSelectAssets);
                openActivity(WalletTransferActivity.class, bundle);
                break;
        }
    }

    private void resetBalanceUI() {
        setText(R.id.tvBalance, Utils.removeZero(mSelectAssets.getBalance()));
        if (mSelectAssets.getSymbol().equalsIgnoreCase("INTEGRAL")) {
            setText(R.id.tvToUsdt, "");
            setText(R.id.tvWithDraw, R.string.app_send_inter);
        } else {
            setText(R.id.tvToUsdt, "≈ $" + Utils.removeZero(mSelectAssets.getBalance()));
        }
    }

    @Override
    public void getAssetsListSuccess(ArrayList<AssetsBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        DataManager.Companion.getInstance().saveAssets(list);
        String symbol = mSelectAssets.getSymbol();
        for (AssetsBean bean : list) {
            if (bean.getSymbol().equals(symbol)) {
                mSelectAssets = bean;
                resetBalanceUI();
                break;
            }
        }
    }

    @Override
    public void getTransferListSuccess(int pageIndex, ArrayList<TransferBean> list) {
        if (isFinish()) {
            return;
        }
        getAdapter().setNewInstance(list);
    }

    private void showExchangeWayList(ArrayList<String> list) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_select_exchange_way);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {

            private int mSelectPos;

            @Override
            public void initView(View view) {
                mSelectPos = 0;
                final ExchangeWayAdapter exchangeWayAdapter = new ExchangeWayAdapter(WalletDetailActivity.this);
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WalletDetailActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                exchangeWayAdapter.onAttachedToRecyclerView(recyclerView);
                recyclerView.setAdapter(exchangeWayAdapter);
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose, R.id.tvOk);
                exchangeWayAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                        mSelectPos = position;
                        exchangeWayAdapter.setSelectPos(position);
                    }
                });
                exchangeWayAdapter.setNewInstance(list);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.tvOk:
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.BUNDLE_EXTRA, mSelectPos);
                        openActivity(WalletExchangeActivity.class, bundle);
                        break;
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }
}
