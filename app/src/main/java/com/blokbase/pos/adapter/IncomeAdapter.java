package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.IncomeBean;

import org.jetbrains.annotations.NotNull;

public class IncomeAdapter extends BaseQuickAdapter<IncomeBean, BaseViewHolder> {

    private Context mContext;

    public IncomeAdapter(Context context) {
        super(R.layout.item_invite);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, IncomeBean bean) {
        helper.setText(R.id.tvTime, bean.getCreateTime().substring(0, 16))
                .setText(R.id.tvUID, bean.getFromId())
                .setText(R.id.tvIdentity, bean.getTypeText())
                .setText(R.id.tvIncome, Utils.removeZero(bean.getAmount()));
    }

}
