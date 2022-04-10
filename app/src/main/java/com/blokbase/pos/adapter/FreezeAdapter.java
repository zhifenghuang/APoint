package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.FreezeBean;

import org.jetbrains.annotations.NotNull;

public class FreezeAdapter extends BaseQuickAdapter<FreezeBean, BaseViewHolder> {

    private Context mContext;

    public FreezeAdapter(Context context) {
        super(R.layout.item_freeze);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FreezeBean bean) {
        String amount = (bean.getAmount().contains("-") ? bean.getAmount().substring(1) : bean.getAmount());
        helper.setText(R.id.tvNum, Utils.removeZero(amount))
                .setText(R.id.tvDate, bean.getUnfreezeTime().substring(0, 10));
    }
}
