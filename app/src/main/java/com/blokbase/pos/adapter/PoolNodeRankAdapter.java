package com.blokbase.pos.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.PoolNodeRankBean;

import org.jetbrains.annotations.NotNull;

public class PoolNodeRankAdapter extends BaseQuickAdapter<PoolNodeRankBean, BaseViewHolder> {

    private Context mContext;

    public PoolNodeRankAdapter(Context context) {
        super(R.layout.item_pool_node_rank);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, PoolNodeRankBean bean) {
        helper.setText(R.id.tvRank, String.valueOf(getItemPosition(bean) + 1))
                .setText(R.id.tvUID, bean.getUserId())
                .setText(R.id.tvPledgeNum, (TextUtils.isEmpty(bean.getPledgeAmount()) ? "0" : Utils.removeZero(bean.getPledgeAmount())) + " UTG")
                .setText(R.id.tvTime, bean.getPledgeTime());
    }
}
