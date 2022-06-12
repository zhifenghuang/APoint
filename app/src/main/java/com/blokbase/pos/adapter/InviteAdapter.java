package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.InviteBean;

import org.jetbrains.annotations.NotNull;

public class InviteAdapter extends BaseQuickAdapter<InviteBean, BaseViewHolder> {

    private Context mContext;

    public InviteAdapter(Context context) {
        super(R.layout.item_invite);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, InviteBean bean) {
        helper.setText(R.id.tvUID, bean.getUserId())
                .setText(R.id.tvInviteAward, Utils.removeZero(bean.getRefereeAmount()))
                .setText(R.id.tvPosPledge, Utils.removeZero(bean.getPledgeAmount()))
                .setText(R.id.tvPosAward, Utils.removeZero(bean.getPosAmount()))
                .setText(R.id.tvPosrAward, Utils.removeZero(bean.getPosrAmount()))
                .setText(R.id.tvTeam, Utils.removeZero(bean.getTeamPledgeAmount()));
    }
}
