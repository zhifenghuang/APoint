package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.GoodsDetailActivity;
import com.blokbase.pos.activity.OrderPayActivity;
import com.blokbase.pos.activity.OrderTrackingActivity;
import com.blokbase.pos.adapter.ExpressNOAdapter;
import com.blokbase.pos.adapter.OrderAdapter;
import com.blokbase.pos.adapter.SkuAdapter;
import com.blokbase.pos.contract.OrderListContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.OrderListPresenter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ExpressNOBean;
import com.common.lib.bean.GoodsSkuBean;
import com.common.lib.bean.OrderBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class OrderListFragment extends BaseFragment<OrderListContract.Presenter>
        implements OrderListContract.View, OnRefreshLoadmoreListener {


    protected int mPageNo;
    protected OrderAdapter mAdapter;
    private int mDeliveryStatus;  //待发货 10 待收货 20 已收货 30


    @NonNull
    @Override
    protected OrderListContract.Presenter onCreatePresenter() {
        return new OrderListPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDeliveryStatus = getArguments().getInt(Constants.BUNDLE_EXTRA);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        SmartRefreshLayout layout = view.findViewById(R.id.smartRefreshLayout);
        layout.setOnRefreshLoadmoreListener(this);
        //     layout.autoRefresh();
        layout.setEnableLoadmore(false);

        getPresenter().orderList(1, mDeliveryStatus);

        getAdapter().setNewInstance(DataManager.Companion.getInstance().getOrderList(mDeliveryStatus));
    }

    protected OrderAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new OrderAdapter(getActivity());
            mAdapter.addChildClickViewIds(R.id.tvViewLogistics, R.id.tvSureReceive);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, final int position) {
                    switch (view.getId()) {
                        case R.id.tvViewLogistics:
                            OrderBean bean = mAdapter.getItem(position);
                            ArrayList<ExpressNOBean> list = new ArrayList<>();
                            if (!TextUtils.isEmpty(bean.getExpressName()) && !TextUtils.isEmpty(bean.getExpressName())) {
                                ExpressNOBean b = new ExpressNOBean();
                                b.setExpressNo(bean.getExpressNo());
                                b.setExpressName(bean.getExpressName());
                                b.setExpressCode(bean.getExpressCode());
                                list.add(b);
                            }
                            if (!TextUtils.isEmpty(bean.getExpressName_1()) && !TextUtils.isEmpty(bean.getExpressName_1())) {
                                ExpressNOBean b = new ExpressNOBean();
                                b.setExpressNo(bean.getExpressNo_1());
                                b.setExpressName(bean.getExpressName_1());
                                b.setExpressCode(bean.getExpressCode_1());
                                list.add(b);
                            }
                            if (!TextUtils.isEmpty(bean.getExpressName_2()) && !TextUtils.isEmpty(bean.getExpressName_2())) {
                                ExpressNOBean b = new ExpressNOBean();
                                b.setExpressNo(bean.getExpressNo_2());
                                b.setExpressName(bean.getExpressName_2());
                                b.setExpressCode(bean.getExpressCode_2());
                                list.add(b);
                            }
                            if (!TextUtils.isEmpty(bean.getExpressName_3()) && !TextUtils.isEmpty(bean.getExpressName_3())) {
                                ExpressNOBean b = new ExpressNOBean();
                                b.setExpressNo(bean.getExpressNo_3());
                                b.setExpressName(bean.getExpressName_3());
                                b.setExpressCode(bean.getExpressCode_3());
                                list.add(b);
                            }
                            showExpressList(bean.getId(), list);
                            break;
                        case R.id.tvSureReceive:
                            showTwoBtnDialog(getString(R.string.app_confirm_receive),
                                    getString(R.string.app_cancel),
                                    getString(R.string.app_ok),
                                    new OnClickCallback() {
                                        @Override
                                        public void onClick(int viewId) {
                                            getPresenter().orderComplete(getAdapter().getItem(position));
                                        }
                                    });
                            break;
                    }
                }
            });
        }
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public void getOrderListSuccess(int page, ArrayList<OrderBean> list) {
        if (getView() == null) {
            return;
        }
        mPageNo = page;
        if (mPageNo == 1) {
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        finishRefreshLoad();
    }

    @Override
    public void getOrderListFailed() {
        finishRefreshLoad();
    }

    @Override
    public void orderCompleteSuccess(OrderBean bean) {
        if (getView() == null) {
            return;
        }
        bean.setDeliveryStatus(30);
        getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        getPresenter().orderList(mPageNo + 1, mDeliveryStatus);
    }

    public void onRefresh() {
        if (getView() == null) {
            return;
        }
        if (getAdapter().getItemCount() == 0) {
            getPresenter().orderList(1, mDeliveryStatus);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getPresenter().orderList(1, mDeliveryStatus);
    }

    protected void finishRefreshLoad() {
        if (getView() == null) {
            return;
        }
        SmartRefreshLayout smartRefreshLayout = getView().findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
        int size = getAdapter().getItemCount();
        smartRefreshLayout.setEnableLoadmore(size != 0 && size % 20 == 0);
        getAdapter().notifyDataSetChanged();
        if (size == 0) {
            setViewGone(R.id.recyclerView);
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewVisible(R.id.recyclerView);
            setViewGone(R.id.tvNoContent);
        }
    }

    public static OrderListFragment getInstance(int deliveryStatus) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.BUNDLE_EXTRA, deliveryStatus);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(String str) {
        if (getView() == null || TextUtils.isEmpty(str)) {
            return;
        }
        if (str.equals(EventBusEvent.REFRESH_ORDER_LIST)) {
            if (mDeliveryStatus == 0 || mDeliveryStatus == 10) {
                getPresenter().orderList(1, mDeliveryStatus);
            }
        }
    }

    private void showExpressList(int orderId, ArrayList<ExpressNOBean> list) {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_select_express_no);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {

            @Override
            public void initView(View view) {
                final ExpressNOAdapter expressNOAdapter = new ExpressNOAdapter(getActivity());
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);
                expressNOAdapter.onAttachedToRecyclerView(recyclerView);
                recyclerView.setAdapter(expressNOAdapter);
                expressNOAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.BUNDLE_EXTRA, orderId);
                        bundle.putSerializable(Constants.BUNDLE_EXTRA_2, expressNOAdapter.getItem(position));
                        openActivity(OrderTrackingActivity.class, bundle);
                        dialogFragment.dismiss();
                    }
                });
                expressNOAdapter.setNewInstance(list);
            }

            @Override
            public void onViewClick(int viewId) {
            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }
}
