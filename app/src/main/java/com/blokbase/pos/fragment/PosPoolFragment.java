package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.GoogleVerifyActivity;
import com.blokbase.pos.activity.MainActivity;
import com.blokbase.pos.activity.ProtocolActivity;
import com.blokbase.pos.activity.SetPasswordActivity;
import com.blokbase.pos.contract.PosPoolContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.PosPoolPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.HomeDataBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.bean.PledgeDataBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.LogUtil;
import com.common.lib.utils.MD5Util;
import com.jakewharton.rxbinding3.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;

import io.reactivex.functions.Consumer;

public class PosPoolFragment extends BaseFragment<PosPoolContract.Presenter> implements PosPoolContract.View {

    private boolean mIsAgreePledge, mIsAgreeCancelPledge;

    @NonNull
    @Override
    protected PosPoolContract.Presenter onCreatePresenter() {
        return new PosPoolPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pos_pool;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mIsAgreePledge = true;
        mIsAgreeCancelPledge = true;
        showData(DataManager.Companion.getInstance().getHomePosData());
        setViewsOnClickListener(R.id.tvPledge, R.id.tvQuit,
                R.id.llProtocol1, R.id.llProtocol2,
                R.id.tvProtocol1, R.id.tvProtocol2);

        final TextView tvPledge = view.findViewById(R.id.tvPledge);
        tvPledge.setBackgroundResource(R.drawable.shape_ededed_9);
        tvPledge.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_4));
        tvPledge.setEnabled(false);
        RxTextView.textChanges(view.findViewById(R.id.etNums)).skip(1).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (TextUtils.isEmpty(getTextById(R.id.etNums))) {
                    tvPledge.setBackgroundResource(R.drawable.shape_ededed_9);
                    tvPledge.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_4));
                    tvPledge.setEnabled(false);
                } else {
                    tvPledge.setBackgroundResource(R.drawable.shape_6961f3_9);
                    tvPledge.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_3));
                    tvPledge.setEnabled(true);
                }
            }
        });

        MetaBean bean = DataManager.Companion.getInstance().getAppMeta();
        double min = 1;
        if (bean != null) {
            min = Double.parseDouble(bean.getPledge().getMin());
        }
        setEditTextHint(R.id.etNums, getString(R.string.app_pledge_min_xxx_utg, Utils.removeZero(String.valueOf(min))));
        initEditText(view.findViewById(R.id.etNums));

        final TextView tvQuit = view.findViewById(R.id.tvQuit);
        tvQuit.setEnabled(false);
        RxTextView.textChanges(view.findViewById(R.id.etQuitNums)).skip(1).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (TextUtils.isEmpty(getTextById(R.id.etQuitNums))) {
                    tvQuit.setEnabled(false);
                } else {
                    tvQuit.setEnabled(true);
                }
            }
        });
        initEditText(view.findViewById(R.id.etQuitNums));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProtocol1:
                mIsAgreePledge = !mIsAgreePledge;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).setImageResource(mIsAgreePledge ? R.drawable.app_checked : R.drawable.app_unchecked);
                break;
            case R.id.llProtocol2:
                mIsAgreeCancelPledge = !mIsAgreeCancelPledge;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).setImageResource(mIsAgreeCancelPledge ? R.drawable.app_checked : R.drawable.app_unchecked);
                break;
            case R.id.tvPledge:
                if (!mIsAgreePledge) {
                    showToast(R.string.app_please_agress_protocol_first);
                    return;
                }
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                if (!myInfo.getPaymentStatus()) {
                    showNoPayPswDialog();
                    return;
                }
                try {
                    double num = Double.parseDouble(getTextById(R.id.etNums));
                    MetaBean bean = DataManager.Companion.getInstance().getAppMeta();
                    double min = 1;
                    if (bean != null) {
                        min = Double.parseDouble(bean.getPledge().getMin());
                    }
                    if (num < min) {
                        showOneBtnDialog(getString(R.string.app_pledge_min_xxx_utg, Utils.removeZero(String.valueOf(min))));
                        return;
                    }
                } catch (Exception e) {
                    return;
                }
                new InputDialog((BaseActivity) getActivity(), new InputDialog.OnInputListener() {

                    @Override
                    public void checkInput(String input) {
                        getPresenter().pledge(getTextById(R.id.etNums), input);
                    }
                }, getString(R.string.app_sure_pay_xxx_utg_pledge, getTextById(R.id.etNums)));
                break;
            case R.id.tvQuit:
                if (!mIsAgreeCancelPledge) {
                    showToast(R.string.app_please_agress_protocol_first);
                    return;
                }
                myInfo = DataManager.Companion.getInstance().getMyInfo();
                if (!myInfo.getPaymentStatus()) {
                    showNoPayPswDialog();
                    return;
                }
                showCancelPledgeDialog();
                break;
            case R.id.tvProtocol1:
            case R.id.tvProtocol2:
                openActivity(ProtocolActivity.class);
                break;
        }
    }

    private void showCancelPledgeDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_cancel_pledge_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                MetaBean bean = DataManager.Companion.getInstance().getAppMeta();
                TextView tvContent1 = view.findViewById(R.id.tvContent1);
                TextView tvContent2 = view.findViewById(R.id.tvContent2);
                if (bean != null) {
                    BigDecimal percent = new BigDecimal(bean.getPledge().getFee()).multiply(new BigDecimal(100));
                    tvContent1.setText(getString(R.string.app_cancel_pledge_content_1, bean.getPledge().getFreezeDays(), bean.getPledge().getFreezeDays()));
                    tvContent2.setText(getString(R.string.app_cancel_pledge_content_2, Utils.removeZero(percent.toString()) + "%"));
                } else {
                    tvContent1.setText(getString(R.string.app_cancel_pledge_content_1, "30", "30"));
                    tvContent2.setText(getString(R.string.app_cancel_pledge_content_2, "10%"));
                }
                dialogFragment.setDialogViewsOnClickListener(view, R.id.btn1, R.id.btn2, R.id.ivClose);
            }

            @Override
            public void onViewClick(int viewId) {
                if (viewId == R.id.ivClose) {
                    return;
                }
                int type = 0;
                if (viewId == R.id.btn1) {
                    type = 1;
                } else if (viewId == R.id.btn2) {
                    type = 0;
                }
                final int freeze = type;
                new InputDialog((BaseActivity) getActivity(), new InputDialog.OnInputListener() {

                    @Override
                    public void checkInput(String input) {
                        getPresenter().cancelPledge(freeze, getTextById(R.id.etQuitNums), input);
                    }
                }, getString(R.string.app_sure_cancel_pledge));
            }
        });
        dialogFragment.show(getChildFragmentManager(), "MyDialogFragment");
    }

    private void showNoPayPswDialog() {
        showTwoBtnDialog(getString(R.string.app_not_set_pay_password_go_set), getString(R.string.app_cancel), getString(R.string.app_ok),
                new OnClickCallback() {
                    @Override
                    public void onClick(int viewId) {
                        if (viewId == R.id.btn2) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Constants.BUNDLE_EXTRA, DataManager.Companion.getInstance().getMyInfo());
                            bundle.putInt(Constants.BUNDLE_EXTRA_2, 3);
                            openActivity(SetPasswordActivity.class, bundle);
                        }
                    }
                });
    }

    public void showData(HomeDataBean bean) {
        if (getView() == null) {
            return;
        }
        if (bean == null) {
            getPresenter().getPledgeData();
        }
        setText(R.id.tvPledgeNum1, Utils.removeZero(bean.getPledgeAmount()) + " UTG");
        setText(R.id.tvPledgeNum2, Utils.removeZero(bean.getPledgeAmount()) + " UTG");
    }

    @Override
    public void getPledgeDataSuccess(PledgeDataBean dataBean) {
        if (getView() == null) {
            return;
        }
        setText(R.id.tvPledgeNum1, Utils.removeZero(dataBean.getPledgeQuantity()) + " UTG");
        setText(R.id.tvPledgeNum2, Utils.removeZero(dataBean.getPledgeQuantity()) + " UTG");
    }

    @Override
    public void pledgeSuccess() {
        if (getView() == null) {
            return;
        }
        setText(R.id.etNums, "");
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventBusEvent.UPDATE_PLEDGE_INFO, "");
        EventBus.getDefault().post(map);
    }

    @Override
    public void cancelPledgeSuccess() {
        if (getView() == null) {
            return;
        }
        setText(R.id.etQuitNums, "");
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventBusEvent.UPDATE_PLEDGE_INFO, "Pledge");
        EventBus.getDefault().post(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (getView() == null) {
            return;
        }
        if (map.containsKey(EventBusEvent.REFRESH_PLEDGE_UI)) {
            showData(DataManager.Companion.getInstance().getHomePosData());
        }
    }

    private void initEditText(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = et.getText().toString().trim();
                if (text.contains(".")) {
                    String[] texts = text.split("\\.");
                    if (texts.length > 1) {
                        String str = texts[1];
                        if (str.length() > 4) {
                            setText(et, text.substring(0, texts[0].length() + 5));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
