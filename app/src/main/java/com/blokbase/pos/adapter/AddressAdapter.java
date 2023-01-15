package com.blokbase.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextUtils;
import android.widget.TextView;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.bean.UserBean;
import com.common.lib.manager.DataManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddressAdapter extends BaseQuickAdapter<ReceiveAddressBean, BaseViewHolder> {

    private Context mContext;
    private boolean mIsEdit;

    public AddressAdapter(Context context) {
        super(R.layout.item_receive_address);
        mContext = context;
        mIsEdit = false;
    }

    public void resetEdit() {
        mIsEdit = !mIsEdit;
        notifyDataSetChanged();
    }

    public void resetDefault(ReceiveAddressBean bean) {
        bean.setDefault(10);
        List<ReceiveAddressBean> list = getData();
        list.remove(bean);
        if (list.size() > 0) {
            list.get(0).setDefault(0);
        }
        list.add(0, bean);
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return mIsEdit;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ReceiveAddressBean bean) {
        if (bean.getDefault() == 0) {
            helper.setImageResource(R.id.ivDefaultIcon, R.drawable.app_address_bg)
                    .setText(R.id.tv, TextUtils.isEmpty(bean.getName()) ? "" : String.valueOf(bean.getName().charAt(0)))
                    .setImageResource(R.id.ivDefault, R.drawable.app_undefault)
                    .setGone(R.id.tvDefault, true);
        } else {
            helper.setImageResource(R.id.ivDefaultIcon, R.drawable.app_address_default)
                    .setText(R.id.tv, "")
                    .setImageResource(R.id.ivDefault, R.drawable.app_default)
                    .setGone(R.id.tvDefault, false);
        }
        if (mIsEdit) {
            helper.setGone(R.id.line1, false)
                    .setGone(R.id.line2, false)
                    .setGone(R.id.line, true)
                    .setGone(R.id.llEdit, false);
        } else {
            helper.setGone(R.id.line1, true)
                    .setGone(R.id.line2, true)
                    .setGone(R.id.line, false)
                    .setGone(R.id.llEdit, true);
        }
        if (getItemPosition(bean) == getItemCount() - 1) {
            helper.setGone(R.id.line, true);
        }

        helper.setText(R.id.tvDetail, bean.getProvinceName() + bean.getCityName() +
                bean.getDistrictName() + bean.getDetail())
                .setText(R.id.tvName, bean.getName())
                .setText(R.id.tvMobile, bean.getMobile());


    }

}
