package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.QuestionBean;

import org.jetbrains.annotations.NotNull;

public class FAQQuestionAdapter extends BaseQuickAdapter<QuestionBean, BaseViewHolder> {

    private Context mContext;

    public FAQQuestionAdapter(Context context) {
        super(R.layout.item_faq_question);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, QuestionBean bean) {
        helper.setText(R.id.tvQuestion, bean.getTitleStr());
    }
}
