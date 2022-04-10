package com.blokbase.pos.adapter;

import android.content.Context;
import android.text.Html;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.NoticeBean;

import org.jetbrains.annotations.NotNull;

public class NoticeAdapter extends BaseQuickAdapter<NoticeBean, BaseViewHolder> {

    private Context mContext;

    public NoticeAdapter(Context context) {
        super(R.layout.item_notice);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, NoticeBean bean) {
        helper.setText(R.id.tvTitle, bean.getTitleStr())
                .setText(R.id.tvTime, bean.getCreateTime())
                .setText(R.id.tvContent, Html.fromHtml(bean.getContentStr()));
    }
}
