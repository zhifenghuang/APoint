package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blokbase.pos.R;
import com.blokbase.pos.adapter.ChainBlockAdapter;
import com.blokbase.pos.contract.ChainDataContract;
import com.blokbase.pos.presenter.ChainDataPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ChainBlockBean;
import com.common.lib.bean.ChainDataBean;
import com.common.lib.manager.DataManager;
import java.util.ArrayList;


public class ChainDataActivity extends BaseActivity<ChainDataContract.Presenter> implements ChainDataContract.View {

    private ChainBlockAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chain_data;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_chain_data_utg);
        getPresenter().chainOverview();
        getPresenter().chainBlock();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        chainOverviewSuccess(DataManager.Companion.getInstance().getChainData());
        chainBlockSuccess(DataManager.Companion.getInstance().getBlockList());
    }

    private ChainBlockAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ChainBlockAdapter(this);
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected ChainDataContract.Presenter onCreatePresenter() {
        return new ChainDataPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void chainOverviewSuccess(ChainDataBean bean) {
        if (bean == null) {
            return;
        }
        setText(R.id.tvHeight, bean.getTotalBlockNumber() + " BLOCK");
        setText(R.id.tvNextHeight, bean.getNextElectTime() + " BLOCK");
        setText(R.id.tvTotalLock, Utils.getNewValue(bean.getLockNum()) + " UTG");
        setText(R.id.tvPosAward, Utils.getNewValue(bean.getUtg24()) + " UTG");
        setText(R.id.tvBalance, Utils.removeZero(bean.getUltronNode()));
        setText(R.id.tvTotalPledge, Utils.removeZero(bean.getUltronNode()) + " UTG");
    }

    @Override
    public void chainBlockSuccess(ArrayList<ChainBlockBean> list) {
        if (list == null) {
            return;
        }
        getAdapter().getData().clear();
        getAdapter().addData(list);
    }

}
