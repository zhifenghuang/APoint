package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ChainBlockBean;
import com.common.lib.utils.BaseUtils;

import org.jetbrains.annotations.NotNull;

public class ChainBlockAdapter extends BaseQuickAdapter<ChainBlockBean, BaseViewHolder> {

    private Context mContext;

    public ChainBlockAdapter(Context context) {
        super(R.layout.item_chain_block);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ChainBlockBean bean) {
        helper.setText(R.id.tvAddress, bean.getMinerAddress())
                .setText(R.id.tvHeight, bean.getBlockNumber())
                .setText(R.id.tvTime, BaseUtils.StaticParams.longToDate3(bean.getTimeStamp()));
    }
}
