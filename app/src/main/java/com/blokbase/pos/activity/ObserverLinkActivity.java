package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.ObserverLinkContract;
import com.blokbase.pos.presenter.ObserverLinkPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ObserverBean;
import com.common.lib.bean.PermissionBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.utils.BaseUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;


public class ObserverLinkActivity extends BaseActivity<ObserverLinkContract.Presenter> implements ObserverLinkContract.View {

    private ObserverBean mObserver;

    private boolean mIncome, mAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_observer_link;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_observer_link);
        setViewsOnClickListener(R.id.tvDelete, R.id.tvCopyLink, R.id.ivEdit);
        mObserver = (ObserverBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvRemark, mObserver.getRemark());
        setText(R.id.tvAccount, mObserver.getAccount());
        setText(R.id.tvSymbol, mObserver.getSymbol());
        setText(R.id.tvLink, mObserver.getLink());
        PermissionBean bean = mObserver.getPermission();
        if (bean.getIncome()) {
            setImage(R.id.ivPermission1, R.drawable.app_choose);
        }
        if (bean.getAccount()) {
            setImage(R.id.ivPermission2, R.drawable.app_choose);
        }
    }

    @NonNull
    @Override
    protected ObserverLinkContract.Presenter onCreatePresenter() {
        return new ObserverLinkPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEdit:
                showEditPermissionDialog();
                break;
            case R.id.tvDelete:
                showDeleteDialog();
                break;
            case R.id.tvCopyLink:
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, mObserver.getLink());
                break;
        }
    }

    private void showDeleteDialog() {
        showTwoBtnDialog(getString(R.string.app_are_you_sure_delete), getString(R.string.app_cancel), getString(R.string.app_ok),
                new OnClickCallback() {
                    @Override
                    public void onClick(int viewId) {
                        if (viewId == R.id.btn2) {
                            getPresenter().deleteObserver(mObserver.getId());
                        }
                    }
                });
    }

    private void showEditPermissionDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_edit_premission_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(@Nullable View view) {
                PermissionBean bean = mObserver.getPermission();
                mIncome = bean.getIncome();
                mAccount = bean.getAccount();
                if (mIncome) {
                    ((ImageView) view.findViewById(R.id.iv1)).setImageResource(R.drawable.app_choose);
                }
                if (mAccount) {
                    ((ImageView) view.findViewById(R.id.iv2)).setImageResource(R.drawable.app_choose);
                }
                dialogFragment.setClickDismiss(false);
                dialogFragment.setDialogViewsOnClickListener(view,
                        R.id.btn1, R.id.btn2, R.id.ll1, R.id.ll2);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.ll1:
                        mIncome = !mIncome;
                        ((ImageView) dialogFragment.getView().findViewById(R.id.iv1)).
                                setImageResource(mIncome ? R.drawable.app_choose : R.drawable.app_not_choose);
                        break;
                    case R.id.ll2:
                        mAccount = !mAccount;
                        ((ImageView) dialogFragment.getView().findViewById(R.id.iv2)).
                                setImageResource(mAccount ? R.drawable.app_choose : R.drawable.app_not_choose);

                        break;
                    case R.id.btn1:
                        dialogFragment.dismiss();
                        break;
                    case R.id.btn2:
                        getPresenter().updateObserver(mObserver.getId(), mObserver.getRemark(), mIncome, mAccount);
                        dialogFragment.dismiss();
                        break;
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    @Override
    public void deleteObserverSuccess() {
        showToast(R.string.app_delete_success);
        HashMap<String, String> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_OBSERVER, mObserver.getId());
        EventBus.getDefault().post(map);
        finish();
    }

    @Override
    public void updateObserverSuccess(boolean income, boolean account) {
        PermissionBean bean = mObserver.getPermission();
        bean.setIncome(income);
        bean.setAccount(account);
        setImage(R.id.ivPermission1, bean.getIncome() ? R.drawable.app_choose : R.drawable.app_not_choose);
        setImage(R.id.ivPermission2, bean.getAccount() ? R.drawable.app_choose : R.drawable.app_not_choose);
        HashMap<String, String> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_OBSERVER, "SHARE");
        EventBus.getDefault().post(map);
    }
}
