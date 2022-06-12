package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.LockIncomeRecordAdapter;
import com.blokbase.pos.adapter.SelectRecordTypeAdapter;
import com.blokbase.pos.contract.LockIncomeContract;
import com.blokbase.pos.presenter.LockIncomePresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ObserverPermissionRecordBean;
import com.common.lib.bean.SelectBean;
import com.common.lib.constant.Constants;
import com.common.lib.dialog.MyDialogFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;

public class LockIncomeRecordActivity extends BaseActivity<LockIncomeContract.Presenter>
        implements LockIncomeContract.View, OnRefreshLoadmoreListener {


    private LockIncomeRecordAdapter mAdapter;
    private int mPageIndex = 0;

    private int mFrom; //0表示POS收益，1表示POSR收益

    private ArrayList<Integer> mTypes;
    private int mSelectType;  //0表示全部

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_income_record;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        mFrom = getIntent().getExtras().getInt(Constants.BUNDLE_EXTRA, 0);
        setText(R.id.tvTitle, mFrom == 0 ? R.string.app_lock_income_record :
                R.string.app_posr_lock_income_record);

        mTypes = new ArrayList<>();
        mSelectType = 0; //全部
        if (mFrom == 0) {
            mTypes.add(10);
            mTypes.add(11);
            mTypes.add(12);
            mTypes.add(13);
        } else {
            mTypes.add(20);
            mTypes.add(21);
            mTypes.add(22);
            mTypes.add(23);
        }
        setText(R.id.tvType, R.string.app_wallet_record_0);
        setViewsOnClickListener(R.id.tvSymbol, R.id.tvType);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        layout.autoRefresh();
        setViewGone(R.id.tvNoContent);
    }

    private LockIncomeRecordAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new LockIncomeRecordAdapter(this, "");
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected LockIncomeContract.Presenter onCreatePresenter() {
        return new LockIncomePresenter(this);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().getLockIncomeRecords(mPageIndex + 1, mTypes);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().getLockIncomeRecords(1, mTypes);
    }

    @Override
    public void getRecordsSuccess(int pageIndex, ArrayList<ObserverPermissionRecordBean> list) {
        if (isFinish()) {
            return;
        }
        mPageIndex = pageIndex;
        if (mPageIndex == 1) {
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishLoad();
    }

    @Override
    public void getRecordsFailed(int code, String msg) {
        finishLoad();
    }

    private void finishLoad() {
        if (isFinish()) {
            return;
        }
        if (getAdapter().getItemCount() == 0) {
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.finishLoadmore();
        layout.finishRefresh();
        layout.setEnableLoadmore(getAdapter().getItemCount() != 0
                && getAdapter().getItemCount() % 20 == 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSymbol:
            case R.id.tvType:
                showSelectRecordTypeDialog();
                break;
        }
    }

    private void showSelectRecordTypeDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_select_record_type_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(@Nullable View view) {
                RecyclerView recyclerView = view.findViewById(R.id.rv);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LockIncomeRecordActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                getAdapter().onAttachedToRecyclerView(recyclerView);
                SelectRecordTypeAdapter adapter = new SelectRecordTypeAdapter(LockIncomeRecordActivity.this);
                recyclerView.setAdapter(adapter);
                int strId = getResources().getIdentifier("app_wallet_record_0",
                        "string", getPackageName());
                adapter.addData(new SelectBean(getString(strId),
                        mSelectType == 0));
                if (mFrom == 0) {
                    for (int i = 10; i < 14; ++i) {
                        strId = getResources().getIdentifier("app_wallet_record_" + i,
                                "string", getPackageName());
                        adapter.addData(new SelectBean(getString(strId),
                                mSelectType == i));
                    }
                } else {
                    for (int i = 20; i < 24; ++i) {
                        strId = getResources().getIdentifier("app_wallet_record_" + i,
                                "string", getPackageName());
                        adapter.addData(new SelectBean(getString(strId),
                                mSelectType == i));
                    }
                }
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        dialogFragment.dismiss();
                        mTypes.clear();

                        if (position == 0) {
                            mSelectType = 0;
                            if (mFrom == 0) {
                                mTypes.add(10);
                                mTypes.add(11);
                                mTypes.add(12);
                                mTypes.add(13);
                            } else {
                                mTypes.add(20);
                                mTypes.add(21);
                                mTypes.add(22);
                                mTypes.add(23);
                            }
                        } else {
                            if (mFrom == 0) {
                                mSelectType = 9 + position;
                            } else {
                                mSelectType = 19 + position;
                            }
                        }
                        if (mSelectType == 0) {
                            setText(R.id.tvType, R.string.app_wallet_record_0);
                        } else {
                            mTypes.add(mSelectType);
                            int strId = getResources().getIdentifier("app_wallet_record_" + mSelectType,
                                    "string", getPackageName());
                            setText(R.id.tvType, strId);
                        }
                        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
                        layout.autoRefresh();
                    }
                });
                dialogFragment.setDialogViewsOnClickListener(view, R.id.tvCancel);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

}
