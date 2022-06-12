package com.blokbase.pos.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ObserverPermissionRecordBean;

import org.jetbrains.annotations.NotNull;

public class LockIncomeRecordAdapter extends BaseQuickAdapter<ObserverPermissionRecordBean, BaseViewHolder> {

    private Context mContext;
    private String mType;

    public LockIncomeRecordAdapter(Context context, String type) {
        super(R.layout.item_lock_income_record);
        mContext = context;
        mType = type;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ObserverPermissionRecordBean bean) {
        helper.setText(R.id.tvAccount, bean.getRemark())
                .setText(R.id.tvAmount, Utils.removeZero(bean.getAmount()))
                .setTextColor(R.id.tvAmount, ContextCompat.getColor(mContext, mType.equals("income") ? R.color.text_color_8 : R.color.text_color_7))
                .setText(R.id.tvLockDays, bean.getUnlockDays())
                .setText(R.id.tvTime, bean.getBlockTime().substring(0, 10));
    }
}
