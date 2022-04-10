package com.blokbase.pos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.FAQContentActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.QuestionBean;
import com.common.lib.bean.TopicBean;
import com.common.lib.constant.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;

public class FAQAdapter extends BaseQuickAdapter<TopicBean, BaseViewHolder> {

    private Context mContext;

    public FAQAdapter(Context context) {
        super(R.layout.item_faq);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, TopicBean bean) {
        helper.setText(R.id.tvTopic, bean.getNameStr());
        ImageView ivArrow = helper.getView(R.id.ivArrow);
        RecyclerView itemRecyclerView = helper.getView(R.id.itemRecyclerView);
        if (bean.isOpen()) {
            itemRecyclerView.setVisibility(View.VISIBLE);
            ivArrow.setRotation(0);
        } else {
            itemRecyclerView.setVisibility(View.GONE);
            ivArrow.setRotation(-90);
        }
        FAQQuestionAdapter adapter;
        if (itemRecyclerView.getAdapter() == null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            itemRecyclerView.setLayoutManager(linearLayoutManager);
            adapter = new FAQQuestionAdapter(mContext);
            adapter.onAttachedToRecyclerView(itemRecyclerView);
            itemRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                    QuestionBean question = ((FAQQuestionAdapter) ad).getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, question);
                    ((BaseActivity) mContext).openActivity(FAQContentActivity.class, bundle);
                }
            });
        } else {
            adapter = (FAQQuestionAdapter) itemRecyclerView.getAdapter();
        }
        Collections.sort(bean.getQuestions(), new Comparator<QuestionBean>() {
            @Override
            public int compare(QuestionBean o1, QuestionBean o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        adapter.setNewInstance(bean.getQuestions());
        adapter.notifyDataSetChanged();
    }
}
