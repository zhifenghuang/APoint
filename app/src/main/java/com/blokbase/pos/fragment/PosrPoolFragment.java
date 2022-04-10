package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.PosPoolContract;
import com.blokbase.pos.presenter.PosPoolPresenter;
import com.common.lib.bean.PledgeDataBean;
import com.common.lib.fragment.BaseFragment;

public class PosrPoolFragment extends BaseFragment<PosPoolContract.Presenter> implements PosPoolContract.View {

    @NonNull
    @Override
    protected PosPoolContract.Presenter onCreatePresenter() {
        return new PosPoolPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pool;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getPledgeDataSuccess(PledgeDataBean dataBean) {

    }

    @Override
    public void pledgeSuccess() {

    }

    @Override
    public void cancelPledgeSuccess() {

    }
}
