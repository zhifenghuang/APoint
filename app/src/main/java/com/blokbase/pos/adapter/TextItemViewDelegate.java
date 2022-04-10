package com.blokbase.pos.adapter;

import android.widget.TextView;

import com.blokbase.pos.R;
import com.common.lib.bean.NoticeBean;
import com.xj.marqueeview.base.ItemViewDelegate;

public class TextItemViewDelegate implements ItemViewDelegate<NoticeBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_marquee_notice;
    }

    @Override
    public boolean isForViewType(NoticeBean item, int position) {
        return true;
    }

    @Override
    public void convert(com.xj.marqueeview.base.ViewHolder holder, NoticeBean noticeBean, int position) {
        TextView tvNotice = holder.getView(R.id.tvNotice);
        tvNotice.setText(noticeBean.getTitleStr());
    }
}
