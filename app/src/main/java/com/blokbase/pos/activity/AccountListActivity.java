package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.AccountAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.HashMap;
import java.util.Map;

public class AccountListActivity extends BaseActivity<EmptyContract.Presenter>
        implements EmptyContract.View {

    protected AccountAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_list;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_switch_account);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        HashMap<String, UserBean> users = DataManager.Companion.getInstance().getLoginUsers();
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
        for (Map.Entry<String, UserBean> entry : users.entrySet()) {
            if (myInfo.getUserId().equals(entry.getKey())) {
                getAdapter().addData(0, entry.getValue());
            } else {
                getAdapter().addData(entry.getValue());
            }
        }
    }

    private AccountAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new AccountAdapter(this);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    UserBean bean = mAdapter.getItem(position);
                    if (bean.getUserId().equalsIgnoreCase(DataManager.Companion.getInstance().getMyInfo().getUserId())) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BUNDLE_EXTRA, bean.getLoginAccount());
                    openActivity(SwitchAccountActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }
}
