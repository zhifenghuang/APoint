package com.blokbase.pos.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.StorageBean;

import org.jetbrains.annotations.NotNull;

public class PosrAddressAdapter extends BaseQuickAdapter<StorageBean, BaseViewHolder> {

    private Context mContext;

    public PosrAddressAdapter(Context context) {
        super(R.layout.item_posr_address);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, StorageBean bean) {
        String address = bean.getAddress();
        helper.setText(R.id.tvDate, bean.getCreateTime().substring(0, 16))
                .setText(R.id.tvAddress, address.substring(0, 6) + "..." + address.substring(address.length() - 6));
        TextView tvStatus = helper.getView(R.id.tvStatus);
        if (bean.getStatus() == 0) {
            tvStatus.setText(mContext.getString(R.string.app_unbind));
            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_5));
            tvStatus.setBackgroundResource(R.drawable.shape_stroke_6961f3_4);
        } else {
            tvStatus.setText(mContext.getString(R.string.app_wait_check));
            tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_2));
            tvStatus.setBackgroundResource(R.drawable.shape_stroke_616161_4);
        }
    }
}
