package com.blokbase.pos.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.TransferBean;

import org.jetbrains.annotations.NotNull;

public class TransferAdapter extends BaseQuickAdapter<TransferBean, BaseViewHolder> {

    private Context mContext;

    public TransferAdapter(Context context) {
        super(R.layout.item_transfer_list);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, TransferBean bean) {
        helper.setText(R.id.tvTime, bean.getCreateTime().substring(0, 10))
                .setText(R.id.tvOrigin, bean.getOrigin());
        if (bean.getStatus() == 20) {
            helper.setText(R.id.tvOrigin, bean.getOrigin() + mContext.getString(R.string.app_failed))
                    .setTextColor(R.id.tvValue, ContextCompat.getColor(mContext, R.color.text_color_2))
                    .setText(R.id.tvValue, Utils.removeZero(bean.getAmount()));
        } else {
            boolean isOut = bean.getDirection().equalsIgnoreCase("OUT");
            helper.setText(R.id.tvOrigin, bean.getOrigin())
                    .setTextColor(R.id.tvValue, ContextCompat.getColor(mContext, isOut ? R.color.text_color_5 : R.color.text_color_7))
                    .setText(R.id.tvValue,
                            ((isOut ? "-" : "+") + Utils.removeZero(bean.getAmount())) + " " + bean.getSymbol2());

            if (bean.getType() == 6 && !isOut
                    && Utils.removeZero(bean.getAmount()).equals("0")) {
                helper.setGone(R.id.tvRemark, false)
                        .setText(R.id.tvRemark, mContext.getString(R.string.app_freeze_xxx,
                                Utils.removeZero(bean.getFreeze()) + " " + bean.getSymbol2()));
            } else if (!Utils.removeZero(bean.getFee()).equals("0")) {
                helper.setGone(R.id.tvRemark, false)
                        .setText(R.id.tvRemark, mContext.getString(R.string.app_fee_xxx,
                                Utils.removeZero(bean.getFee()) + " " + bean.getSymbol2()));
            } else {
                helper.setGone(R.id.tvRemark, true);
            }
        }
    }
}
