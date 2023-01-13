package com.blokbase.pos.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.OrderLogisticsBean;

import org.jetbrains.annotations.NotNull;

public class OrderTrackingAdapter extends BaseQuickAdapter<OrderLogisticsBean, BaseViewHolder> {

    private Context mContext;

    public OrderTrackingAdapter(Context context) {
        super(R.layout.item_order_tracking);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderLogisticsBean bean) {
        int position = getItemPosition(bean);
        TextView tvText = helper.getView(R.id.tvText);
        if (position == 0) {
            helper.setGone(R.id.iv, false)
                    .setGone(R.id.dot, true)
                    .setGone(R.id.tvTime, true)
                    .setImageResource(R.id.iv, R.drawable.app_receive)
                    .setText(R.id.tvText, bean.getContext());
            tvText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_2));
        } else if (position == 1) {
            helper.setGone(R.id.iv, false)
                    .setGone(R.id.dot, true)
                    .setGone(R.id.tvTime, false)
                    .setImageResource(R.id.iv, R.drawable.app_default)
                    .setText(R.id.tvText, bean.getContext())
                    .setText(R.id.tvTime, bean.getTime());
            tvText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_1));
        } else {
            helper.setGone(R.id.iv, true)
                    .setGone(R.id.dot, false)
                    .setGone(R.id.tvTime, false)
                    .setImageResource(R.id.iv, R.drawable.app_default)
                    .setText(R.id.tvText, bean.getContext())
                    .setText(R.id.tvTime, bean.getTime());
            tvText.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_1));
        }
    }

}
