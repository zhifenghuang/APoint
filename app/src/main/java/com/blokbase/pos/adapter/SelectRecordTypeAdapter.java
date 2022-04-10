package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.SelectBean;

import org.jetbrains.annotations.NotNull;

public class SelectRecordTypeAdapter extends BaseQuickAdapter<SelectBean, BaseViewHolder> {

    private Context mContext;

    public SelectRecordTypeAdapter(Context context) {
        super(R.layout.item_select_record_type);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, SelectBean bean) {
        helper.setText(R.id.tvType, bean.getText())
                .setVisible(R.id.ivCheck, bean.isSelect())
                .setGone(R.id.line, getItemPosition(bean) == getItemCount() - 1);
    }
}
