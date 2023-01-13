package com.blokbase.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.SubmitOrderContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.SubmitOrderPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.GoodsBean;
import com.common.lib.bean.GoodsSkuBean;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.interfaces.OnClickCallback;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderPayActivity extends BaseActivity<SubmitOrderContract.Presenter>
        implements SubmitOrderContract.View {

    private ReceiveAddressBean mSelectReceiveAddress;
    private GoodsBean mSelectGoods;
    private GoodsSkuBean mSelectSku;
    private int mNum;
    private String mOnePrice;

    private ActivityResultLauncher mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                mSelectReceiveAddress = (ReceiveAddressBean) data.getSerializableExtra(Constants.BUNDLE_EXTRA);
                setText(R.id.tvAddress, mSelectReceiveAddress.getProvinceName() +
                        mSelectReceiveAddress.getCityName() + mSelectReceiveAddress.getDistrictName() + mSelectReceiveAddress.getDetail());
            }
        }
    });

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_buy_pay);
        setViewsOnClickListener(R.id.tvAddress, R.id.tvSubmit, R.id.ivReduce, R.id.ivAdd);
        getPresenter().getDefaultAddress();
        Bundle bundle = getIntent().getExtras();
        mSelectGoods = (GoodsBean) bundle.getSerializable(Constants.BUNDLE_EXTRA);
        mSelectSku = (GoodsSkuBean) bundle.getSerializable(Constants.BUNDLE_EXTRA_2);
        mNum = bundle.getInt(Constants.BUNDLE_EXTRA_3, 1);
        setText(R.id.tvNum, String.valueOf(mNum));
        BaseUtils.StaticParams.loadImage(this, 0, mSelectGoods.getCoverPic(), findViewById(R.id.ivGoods));
        setText(R.id.tvName1, mSelectGoods.getGoodsName() + "");
        if (mSelectSku == null) {
            mOnePrice = mSelectGoods.getSalePrice();
            setText(R.id.tvName2, "");
        } else {
            mOnePrice = mSelectSku.getSalePrice();
            setText(R.id.tvName2, mSelectSku.getSku() + "");
        }
        String[] price = mOnePrice.split("\\.");
        setText(R.id.tvOnePrice1, price[0]);
        setText(R.id.tvOnePrice2, (price.length == 1 ? ".00" : "." + price[1]) + " " + getString(mSelectGoods.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points));
        resetPrice();
    }

    @NonNull
    @Override
    protected SubmitOrderContract.Presenter onCreatePresenter() {
        return new SubmitOrderPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivReduce:
                calculateNum(false);
                break;
            case R.id.ivAdd:
                calculateNum(true);
                break;
            case R.id.tvAddress:
                mLauncher.launch(new Intent(this, AddressListActivity.class));
                break;
            case R.id.tvSubmit:
                if (mSelectReceiveAddress == null) {
                    showToast(R.string.app_select_address);
                    return;
                }
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                if (!myInfo.getPaymentStatus()) {
                    showNoPayPswDialog();
                    return;
                }
                final ArrayList<HashMap<String, Object>> goods = new ArrayList<>();
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", mSelectGoods.getId());
                map.put("buyNum", mNum);
                map.put("goodsName", mSelectGoods.getGoodsName());
                if (mSelectSku != null) {
                    map.put("sku", mSelectSku.getSku());
                }
                goods.add(map);
                new InputDialog(this, new InputDialog.OnInputListener() {

                    @Override
                    public void checkInput(String input) {
                        getPresenter().submitOrder(mSelectReceiveAddress.getId(), mSelectGoods.getGoodsType(),
                                input, goods);
                    }
                }, getString(R.string.app_please_input_your_pay_psw));
                break;
        }
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

    private void calculateNum(boolean isAdd) {
        try {
            TextView tv = findViewById(R.id.tvNum);
            if (isAdd) {
                ++mNum;
                setText(tv, String.valueOf(mNum));
            } else {
                --mNum;
                if (mNum < 1) {
                    mNum = 1;
                }
                setText(tv, String.valueOf(mNum));
            }
            resetPrice();
        } catch (Exception e) {

        }
    }

    private void resetPrice() {
        String[] price = Utils.removeZero(String.valueOf(Double.parseDouble(mOnePrice) * mNum + 0.0000001)).split("\\.");
        setText(R.id.tvPrice1, price[0]);
        setText(R.id.tvPrice2, (price.length == 1 ? ".00" : "." + price[1]) + " " + getString(mSelectGoods.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points));
    }

    @Override
    public void submitOrderSuccess() {
        if (isFinish()) {
            return;
        }
        finish();
        EventBus.getDefault().post(EventBusEvent.REFRESH_ORDER_LIST);
        showToast(R.string.app_order_success);
    }

    @Override
    public void getDefaultAddressSuccess(ReceiveAddressBean bean) {
        mSelectReceiveAddress = bean;
        if (bean != null && !isFinish()) {
            setText(R.id.tvAddress, bean.getProvinceName() + bean.getCityName() + bean.getDistrictName() + bean.getDetail());
        }
    }
}
