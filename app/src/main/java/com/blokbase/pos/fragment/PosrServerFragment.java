package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.PosrLinkActivity;
import com.blokbase.pos.activity.ProtocolActivity;
import com.blokbase.pos.adapter.PosrAddressAdapter;
import com.blokbase.pos.contract.PosrServerContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.PosrServerPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.MetaBean;
import com.common.lib.bean.PosrLinkBean;
import com.common.lib.bean.StorageBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;

import java.util.ArrayList;

public class PosrServerFragment extends BaseFragment<PosrServerContract.Presenter> implements PosrServerContract.View {

    private boolean mIsAgreePledge;

    private PosrAddressAdapter mSelfAdapter;

    @NonNull
    @Override
    protected PosrServerContract.Presenter onCreatePresenter() {
        return new PosrServerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_posr_server;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.tvBind, R.id.tvIncomeAddress,
                R.id.llProtocol1,
                R.id.tvProtocol1);

        mIsAgreePledge = true;

        getMetaSuccess(DataManager.Companion.getInstance().getAppMeta());
        getPresenter().appMeta();

        RecyclerView recyclerView = view.findViewById(R.id.rvSelf);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getSelfAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getSelfAdapter());
    }

    private PosrAddressAdapter getSelfAdapter() {
        if (mSelfAdapter == null) {
            mSelfAdapter = new PosrAddressAdapter(getActivity());
            mSelfAdapter.addChildClickViewIds(R.id.tvStatus);
            mSelfAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    if (view.getId() == R.id.tvStatus) {
                        StorageBean bean = mSelfAdapter.getItem(position);
                        if (bean.getStatus() == 0) {
                            new InputDialog((BaseActivity) getActivity(), new InputDialog.OnInputListener() {

                                @Override
                                public void checkInput(String input) {
                                    getPresenter().cancelStorage("OWNER", bean, input);
                                }
                            }, getString(R.string.app_are_you_sure_unbind));
                        }
                    }
                }
            });
        }
        return mSelfAdapter;
    }

    public void onResume() {
        super.onResume();
        getPresenter().getStorageList(1, "OWNER");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvIncomeAddress:
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(getActivity(), ((TextView) v).getText().toString());
                break;
            case R.id.llProtocol1:
                mIsAgreePledge = !mIsAgreePledge;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).setImageResource(mIsAgreePledge ? R.drawable.app_checked : R.drawable.app_unchecked);
                break;
            case R.id.tvBind:
                if (!mIsAgreePledge) {
                    showToast(R.string.app_please_agress_protocol_first);
                    return;
                }
                getPresenter().getPosrLink("OWNER");//类型 HOSTING托管 OWNER自管
                break;
            case R.id.tvProtocol1:
                openActivity(ProtocolActivity.class);
                break;
        }
    }

    @Override
    public void getStorageListSuccess(String type, int pageNo, ArrayList<StorageBean> list) {
        if (getView() == null) {
            return;
        }
        getSelfAdapter().setNewInstance(list);
        if (getSelfAdapter().getItemCount() == 0) {
            setViewGone(R.id.rvSelf);
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewVisible(R.id.rvSelf);
            setViewGone(R.id.tvNoContent);
        }
    }

    @Override
    public void getPosrLinkSuccess(PosrLinkBean posrLink) {
        if (getView() == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_EXTRA, posrLink);
        openActivity(PosrLinkActivity.class, bundle);
    }

    @Override
    public void cancelStorageSuccess(String type, StorageBean bean) {
        if (getView() == null) {
            return;
        }
        bean.setStatus(1);
        getSelfAdapter().notifyDataSetChanged();
        showToast(R.string.app_unbind_success);
    }

    @Override
    public void getMetaSuccess(MetaBean bean) {
        if (bean != null) {
            setViewVisible(R.id.llIncomeAddress);
            String address = bean.getNode().getPOSR_RevenuesAddress();
            setText(R.id.tvIncomeAddress, address.substring(0, 8) + "..." + address.substring(address.length() - 8));
        } else {
            setViewGone(R.id.llIncomeAddress);
        }
    }

}
