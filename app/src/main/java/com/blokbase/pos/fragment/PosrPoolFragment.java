package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.ArrayList;

public class PosrPoolFragment extends BaseFragment<EmptyContract.Presenter> implements EmptyContract.View {

    private ArrayList<BaseFragment> mBaseFragment;

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_posr_pool;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new PosrServerFragment());
        switchFragment(mBaseFragment.get(0));
    }

    @Override
    public void onClick(View v) {

    }


    public int getContainerViewId() {
        return R.id.fl;
    }

    public void switchToFragment(int index) {
        switchFragment(mBaseFragment.get(index));
    }
}
