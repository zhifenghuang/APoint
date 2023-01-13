package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.CheckDayBean;

import org.jetbrains.annotations.NotNull;

public class MarkedAdapter extends BaseQuickAdapter<CheckDayBean, BaseViewHolder> {

    private Context mContext;
    private int mCheckCount;

    public MarkedAdapter(Context context) {
        super(R.layout.item_mark);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, CheckDayBean bean) {
        helper.setText(R.id.tvDay, mContext.getString(R.string.app_xxx_day, String.valueOf(bean.getIndex())))
                .setText(R.id.tvValue, bean.getAmount());
        if (bean.getIndex() <= mCheckCount) {
            helper.getView(R.id.ll).setAlpha(0.4f);
            helper.setGone(R.id.ll1, true)
                    .setGone(R.id.iv, false)
                    .setGone(R.id.tv, false);
        } else {
            helper.getView(R.id.ll).setAlpha(1.0f);
            helper.setGone(R.id.ll1, false)
                    .setGone(R.id.iv, true)
                    .setGone(R.id.tv, true);
        }
    }

    public void setCheckedCount(int count) {
        mCheckCount = count;
        notifyDataSetChanged();
    }

}
