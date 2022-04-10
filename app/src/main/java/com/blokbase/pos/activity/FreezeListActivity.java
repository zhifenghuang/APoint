package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.FreezeAdapter;
import com.blokbase.pos.contract.FreezeListContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.FreezeListPresenter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.FreezeBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class FreezeListActivity extends BaseActivity<FreezeListContract.Presenter>
        implements FreezeListContract.View, OnRefreshLoadmoreListener {

    protected int mPageNo;
    protected FreezeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_freeze_list;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_freeze);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        layout.autoRefresh();
        layout.setEnableLoadmore(false);
    }

    protected FreezeAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new FreezeAdapter(this);
            mAdapter.addChildClickViewIds(R.id.tvOperator);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    if (view.getId() == R.id.tvOperator) {
                        showCancelPledgeDialog(mAdapter.getItem(position));
                    }
                }
            });
        }
        return mAdapter;
    }

    private void showCancelPledgeDialog(final FreezeBean bean) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_cancel_pledge_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                MetaBean bean = DataManager.Companion.getInstance().getAppMeta();
                ((TextView) view.findViewById(R.id.btn1)).setText(getString(R.string.app_cancel));
                TextView tvContent1 = view.findViewById(R.id.tvContent1);
                tvContent1.setVisibility(View.GONE);
                TextView tvContent2 = view.findViewById(R.id.tvContent2);
                if (bean != null) {
                    BigDecimal percent = new BigDecimal(bean.getPledge().getFee()).multiply(new BigDecimal(100));
                    tvContent2.setText(getString(R.string.app_cancel_pledge_content_2, Utils.removeZero(percent.toString()) + "%"));
                } else {
                    tvContent2.setText(getString(R.string.app_cancel_pledge_content_2, "10%"));
                }
                dialogFragment.setDialogViewsOnClickListener(view, R.id.btn1, R.id.btn2, R.id.ivClose);
            }

            @Override
            public void onViewClick(int viewId) {
                if (viewId == R.id.btn2) {
                    new InputDialog(FreezeListActivity.this, new InputDialog.OnInputListener() {

                        @Override
                        public void checkInput(String input) {
                            getPresenter().unlock(bean, input);
                        }
                    }, getString(R.string.app_sure_freeze));
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    @NonNull
    @Override
    protected FreezeListContract.Presenter onCreatePresenter() {
        return new FreezeListPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getFreezeListSuccess(int page, ArrayList<FreezeBean> list) {
        mPageNo = page;
        if (mPageNo == 1) {
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishRefreshLoad();
    }

    @Override
    public void getFreezeListFailed() {
        finishRefreshLoad();
    }

    @Override
    public void unlockSuccess(FreezeBean bean) {
        if (isFinish()) {
            return;
        }
        getAdapter().remove(bean);
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_ASSETS, "");
        EventBus.getDefault().post(map);
        finish();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().freezeList(mPageNo + 1);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().freezeList(1);
    }

    protected void finishRefreshLoad() {
        SmartRefreshLayout smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        int size = getAdapter().getItemCount();
        smartRefreshLayout.setEnableLoadmore(size != 0 && size % 20 == 0);
        getAdapter().notifyDataSetChanged();
        if (size == 0) {
            setViewVisible(R.id.tvNoContent);
        }
    }
}
