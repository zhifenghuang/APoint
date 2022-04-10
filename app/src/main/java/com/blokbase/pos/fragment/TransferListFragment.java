package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.AssetsRecordDetailActivity;
import com.blokbase.pos.adapter.SelectRecordTypeAdapter;
import com.blokbase.pos.adapter.TransferAdapter;
import com.blokbase.pos.contract.TransferListContract;
import com.blokbase.pos.presenter.TransferListPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.SelectBean;
import com.common.lib.bean.TransferBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class TransferListFragment extends BaseFragment<TransferListContract.Presenter>
        implements TransferListContract.View, OnRefreshLoadmoreListener {

    private TransferAdapter mAdapter;

    private String mSymbol;
    private int mDirection;
    private int mPageIndex = 0;
    private ArrayList<Integer> mTypes;
    private int mSelectType;  //0表示全部


    @NonNull
    @Override
    protected TransferListContract.Presenter onCreatePresenter() {
        return new TransferListPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet_detail;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mSymbol = getArguments().getString(Constants.BUNDLE_EXTRA);
        mDirection = getArguments().getInt(Constants.BUNDLE_EXTRA_2);
        int from = getArguments().getInt(Constants.BUNDLE_EXTRA_3);
        mTypes = new ArrayList<>();
        if (from == 1) {
            setViewVisible(R.id.ll);
            setViewsOnClickListener(R.id.tvSymbol, R.id.tvType);
            if (mDirection == 0) {
                mSelectType = 0; //全部
                setText(R.id.tvType, R.string.app_wallet_record_0);
            } else {
                mSelectType = 2; //提币
                setText(R.id.tvType, R.string.app_wallet_record_2);
                mTypes.add(2);
            }
        } else {
            if (mDirection == 1) {  //充提记录
                mTypes.add(0);
                mTypes.add(1);
                mTypes.add(2);
            }
        }
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = view.findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.setEnableLoadmore(false);
        layout.autoRefresh();
        setViewGone(R.id.tvNoContent);
    }

    private TransferAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new TransferAdapter(getActivity());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSymbol:
            case R.id.tvType:
                showSelectRecordTypeDialog();
                break;
        }
    }

    public static TransferListFragment getInstance(String symbol, int index, int from) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_EXTRA, symbol);
        bundle.putInt(Constants.BUNDLE_EXTRA_2, index);
        bundle.putInt(Constants.BUNDLE_EXTRA_3, from);
        TransferListFragment fragment = new TransferListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().transferList(mSymbol, mPageIndex + 1, mTypes);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().transferList(mSymbol, 1, mTypes);
    }

    @Override
    public void getTransferListSuccess(int pageIndex, ArrayList<TransferBean> list) {
        if (getView() == null) {
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
    public void getTransferListFailed() {
        finishLoad();
    }

    private void finishLoad() {
        if (getView() == null) {
            return;
        }
        if (getAdapter().getItemCount() == 0) {
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewGone(R.id.tvNoContent);
        }
        SmartRefreshLayout layout = getView().findViewById(R.id.smartRefreshLayout);
        layout.finishLoadmore();
        layout.finishRefresh();
        layout.setEnableLoadmore(getAdapter().getItemCount() != 0
                && getAdapter().getItemCount() % 20 == 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (getView() == null || map == null) {
            return;
        }
        if (map.containsKey(EventBusEvent.REFRESH_ASSETS)) {
            SmartRefreshLayout layout = getView().findViewById(R.id.smartRefreshLayout);
            layout.autoRefresh();
        }
    }

    private void showSelectRecordTypeDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_select_record_type_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(@Nullable View view) {
                RecyclerView recyclerView = view.findViewById(R.id.rv);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                getAdapter().onAttachedToRecyclerView(recyclerView);
                SelectRecordTypeAdapter adapter = new SelectRecordTypeAdapter(getActivity());
                recyclerView.setAdapter(adapter);
                if (mDirection == 0) {
                    int strId;
                    for (int i = 0; i < 8; ++i) {
                        strId = getResources().getIdentifier("app_wallet_record_" + i,
                                "string", getActivity().getPackageName());
                        adapter.addData(new SelectBean(getString(strId),
                                mSelectType == i));
                    }
                } else {
                    int strId;
                    for (int i = 1; i < 3; ++i) {
                        strId = getResources().getIdentifier("app_wallet_record_" + i,
                                "string", getActivity().getPackageName());
                        adapter.addData(new SelectBean(getString(strId),
                                mSelectType == i));
                    }
                }
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        dialogFragment.dismiss();
                        mTypes.clear();

                        if (mDirection == 0) {
                            mSelectType = position;
                        } else {
                            mSelectType = position + 1;
                        }
                        if (mSelectType == 0) {
                            setText(R.id.tvType, R.string.app_wallet_record_0);
                        } else {
                            if (mSelectType == 2) {
                                mTypes.add(0);//内转也包含
                            }
                            mTypes.add(mSelectType);
                            int strId = getResources().getIdentifier("app_wallet_record_" + mSelectType,
                                    "string", getActivity().getPackageName());
                            setText(R.id.tvType, strId);
                        }
                        SmartRefreshLayout layout = getView().findViewById(R.id.smartRefreshLayout);
                        layout.autoRefresh();
                    }
                });
                dialogFragment.setDialogViewsOnClickListener(view, R.id.tvCancel);
            }

            @Override
            public void onViewClick(int viewId) {

            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }
}
