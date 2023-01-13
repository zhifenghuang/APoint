package com.blokbase.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.AddressAdapter;
import com.blokbase.pos.adapter.CityAdapter;
import com.blokbase.pos.contract.AddressListContract;
import com.blokbase.pos.presenter.AddressListPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressListActivity extends BaseActivity<AddressListContract.Presenter>
        implements AddressListContract.View {

    protected AddressAdapter mAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_my_address_list);
        TextView tvRight = findViewById(R.id.tvRight);
        tvRight.setText(R.string.app_manage);
        tvRight.setVisibility(View.VISIBLE);
        setViewsOnClickListener(R.id.tvRight);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        getPresenter().addressList();
        setViewsOnClickListener(R.id.tvAdd, R.id.ivEdit);
    }

    private AddressAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new AddressAdapter(this);
            mAdapter.addChildClickViewIds(R.id.ivEdit, R.id.ivDefault, R.id.tvDelete);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    switch (view.getId()) {
                        case R.id.ivDefault:
                            getPresenter().setAddressDefault(mAdapter.getItem(position));
                            break;
                        case R.id.tvDelete:
                            showTwoBtnDialog(getString(R.string.app_are_you_sure_delete), getString(R.string.app_cancel), getString(R.string.app_ok),
                                    new OnClickCallback() {
                                        @Override
                                        public void onClick(int viewId) {
                                            if (viewId == R.id.btn2) {
                                                getPresenter().deleteAddress(mAdapter.getItem(position));
                                            }
                                        }
                                    });
                            break;
                        case R.id.ivEdit:
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.BUNDLE_EXTRA, 1);
                            bundle.putSerializable(Constants.BUNDLE_EXTRA_2, mAdapter.getItem(position));
                            openActivity(AddAddressActivity.class, bundle);
                            break;
                    }
                }
            });
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            });
        }
        return mAdapter;
    }


    @NonNull
    @Override
    protected AddressListContract.Presenter onCreatePresenter() {
        return new AddressListPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                getAdapter().resetEdit();
                setText(R.id.tvRight, getAdapter().isEdit() ? R.string.app_complete : R.string.app_manage);
                break;
            case R.id.tvAdd:
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.BUNDLE_EXTRA, 0);
                openActivity(AddAddressActivity.class, bundle);
                break;
        }
    }

    @Override
    public void getAddressListSuccess(ArrayList<ReceiveAddressBean> list) {
        if (isFinish()) {
            return;
        }
        getAdapter().setNewInstance(list);
        showNoAddress();
    }

    @Override
    public void deleteAddressSuccess(ReceiveAddressBean bean) {
        if (isFinish()) {
            return;
        }
        getAdapter().remove(bean);
        showNoAddress();
    }

    private void showNoAddress() {
        if (getAdapter().getItemCount() == 0) {
            setViewGone(R.id.recyclerView);
            setViewVisible(R.id.rlNoAddress);
        } else {
            setViewVisible(R.id.recyclerView);
            setViewGone(R.id.rlNoAddress);
        }
    }

    @Override
    public void setAddressDefaultSuccess(ReceiveAddressBean bean) {
        if (isFinish()) {
            return;
        }
        getAdapter().resetDefault(bean);
    }

    @Override
    public void onReceive(HashMap<String, Object> map) {
        if (!isFinish() && map != null && map.containsKey(EventBusEvent.REFRESH_ADDRESS_LIST)) {
            getPresenter().addressList();
        }
    }
}
