package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.OrderTrackingAdapter;
import com.blokbase.pos.contract.OrderTrackingContract;
import com.blokbase.pos.presenter.OrderTrackingPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ExpressNOBean;
import com.common.lib.bean.OrderBean;
import com.common.lib.bean.OrderLogisticsBean;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.constant.Constants;
import com.common.lib.utils.BaseUtils;

import java.util.List;

public class OrderTrackingActivity extends BaseActivity<OrderTrackingContract.Presenter>
        implements OrderTrackingContract.View {

    protected OrderTrackingAdapter mAdapter;
    private OrderBean mOrder;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_tracing;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_order_tracking);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        setViewsOnClickListener(R.id.tvCopy);
        setTextViewLinearGradient(R.id.tvCopy);

        int orderId = getIntent().getExtras().getInt(Constants.BUNDLE_EXTRA);
        ExpressNOBean bean = (ExpressNOBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA_2);
        getPresenter().orderLogistics(orderId, bean.getExpressCode(), bean.getExpressNo());
    }

    protected OrderTrackingAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new OrderTrackingAdapter(this);
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected OrderTrackingContract.Presenter onCreatePresenter() {
        return new OrderTrackingPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCopy:
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, mOrder.getOrderNo());
                break;
        }
    }

    @Override
    public void getOrderLogisticsSuccess(OrderBean orderBean) {
        if (isFinish()) {
            return;
        }
        mOrder = orderBean;
        try {
            setViewVisible(R.id.tvStatus, R.id.ll);
            setText(R.id.tvExpressName, getString(R.string.app_carrier) + orderBean.getExpressName());
            setText(R.id.tvExpressNo, orderBean.getExpressNo());
            List<OrderLogisticsBean> list = orderBean.getLogistics();
            if (list != null && !list.isEmpty()) {
                setText(R.id.tvStatus, list.get(0).getStatus());
            }
            OrderLogisticsBean bean = new OrderLogisticsBean();
            ReceiveAddressBean address = orderBean.getAddress();
            bean.setContext(getString(R.string.app_receive_address_1)
                    + address.getProvinceName() + address.getCityName()
                    + address.getDistrictName() + address.getDetail());
            list.add(0, bean);
            getAdapter().setNewInstance(list);
        } catch (Exception e) {

        }

    }
}
