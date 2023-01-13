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
        int stringId=mContext.getResources().getIdentifier("app_identity_"+bean.getGradeId(),"string",mContext.getPackageName());
        helper.setText(R.id.tvTime, bean.getCreateTime().substring(0, 16))
                .setText(R.id.tvUID, bean.getUserId())
                .setText(R.id.tvIdentity, mContext.getString(stringId))
                .setText(R.id.tvIncome, Utils.removeZero(bean.getRefereeAmount()));
    }

}
