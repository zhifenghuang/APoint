package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ExpressNOBean;

import org.jetbrains.annotations.NotNull;

public class ExchangeWayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public ExchangeWayAdapter(Context context) {
        super(R.layout.item_express_no);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String bean) {
        helper.setText(R.id.tvText, bean);
    }
}
