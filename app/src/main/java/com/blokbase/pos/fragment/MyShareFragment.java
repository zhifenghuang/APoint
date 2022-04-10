package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.AddObserverActivity;
import com.blokbase.pos.activity.InputGoogleCodeActivity;
import com.blokbase.pos.activity.ObserverActivity;
import com.blokbase.pos.activity.ObserverLinkActivity;
import com.blokbase.pos.activity.ObserverPermissionRecordActivity;
import com.blokbase.pos.adapter.ObserverAdapter;
import com.blokbase.pos.contract.ObserverContract;
import com.blokbase.pos.presenter.ObserverPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.bean.ObserverBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class MyShareFragment extends BaseFragment<ObserverContract.Presenter>
        implements ObserverContract.View, OnRefreshLoadmoreListener {

    private ObserverAdapter mAdapter;
    private int mPageIndex = 0;
    protected String mType;
    private boolean mIsEdit;

    @NonNull
    @Override
    protected ObserverContract.Presenter onCreatePresenter() {
        return new ObserverPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_share;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mType = "SHARE";
        setViewsOnClickListener(R.id.tvCreateObserver);
        initView(view);
    }

    public void setEdit(boolean isEdit) {
        mIsEdit = isEdit;
        if (getView() == null) {
            return;
        }
        getAdapter().setEdit(isEdit);
    }

    protected void initView(View view) {
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

    private ObserverAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ObserverAdapter(getActivity());
            mAdapter.addChildClickViewIds(R.id.iv);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    switch (view.getId()) {
                        case R.id.iv:
                            if (mIsEdit) {
                                showDeleteDialog(mAdapter.getItem(position));
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                                openActivity(mType.equals("SHARE") ?
                                        ObserverLinkActivity.class : ObserverPermissionRecordActivity.class, bundle);
                            }
                            break;
                    }
                }
            });
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    openActivity(mType.equals("SHARE") ?
                            ObserverLinkActivity.class : ObserverPermissionRecordActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }

    private void showDeleteDialog(ObserverBean bean) {
        showTwoBtnDialog(getString(R.string.app_are_you_sure_delete), getString(R.string.app_cancel), getString(R.string.app_ok),
                new OnClickCallback() {
                    @Override
                    public void onClick(int viewId) {
                        if (viewId == R.id.btn2) {
                            getPresenter().deleteObserver(bean);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCreateObserver:
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                if (!myInfo.getAuthStatus()) {
                    ((ObserverActivity) getActivity()).showBindGoogleDialog();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_EXTRA, 0);
                openActivity(InputGoogleCodeActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().getObservers(mPageIndex + 1, mType);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().getObservers(1, mType);
    }

    @Override
    public void addObserverSuccess(int pageIndex, ArrayList<ObserverBean> list) {
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
    public void addObserverFailed() {
        finishLoad();
    }

    @Override
    public void deleteObserverSuccess(ObserverBean bean) {
        if (getView() == null) {
            return;
        }
        getAdapter().remove(bean);
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
        if (map.containsKey(EventBusEvent.REFRESH_OBSERVER)) {
            String value = (String) map.get(EventBusEvent.REFRESH_OBSERVER);
            if (value.equals("SHARE") || value.equals("COLLECT")) {
                if (value.equals(mType)) {
                    SmartRefreshLayout layout = getView().findViewById(R.id.smartRefreshLayout);
                    layout.autoRefresh();
                }
            } else {
                getAdapter().deleteById(value);
            }
        }
    }
}
