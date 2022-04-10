package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.blokbase.pos.R;
import com.blokbase.pos.adapter.FAQAdapter;
import com.blokbase.pos.contract.FAQContract;
import com.blokbase.pos.presenter.FAQPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.QuestionBean;
import com.common.lib.bean.TopicBean;
import com.common.lib.manager.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FAQActivity extends BaseActivity<FAQContract.Presenter> implements FAQContract.View {

    private FAQAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_faq;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_faq);
        getPresenter().faq(1);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());

        getFaqSuccess(DataManager.Companion.getInstance().getFAQs());
    }

    private FAQAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new FAQAdapter(this);
            mAdapter.addChildClickViewIds(R.id.llTopic);
            mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                    switch (view.getId()) {
                        case R.id.llTopic:
                            TopicBean bean = mAdapter.getItem(position);
                            bean.setOpen(!bean.isOpen());
                            getAdapter().notifyDataSetChanged();
                            break;
                    }
                }
            });
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected FAQContract.Presenter onCreatePresenter() {
        return new FAQPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void getFaqSuccess(ArrayList<QuestionBean> list) {
        if (isFinish() || list == null || list.isEmpty()) {
            return;
        }
        ArrayList<TopicBean> topics = new ArrayList<>();
        for (QuestionBean bean : list) {
            TopicBean topic = bean.getTopic();
            boolean isContain = false;
            for (TopicBean t : topics) {
                if (t.getId() == topic.getId()) {
                    isContain = true;
                    t.getQuestions().add(bean);
                    break;
                }
            }
            if (!isContain) {
                topics.add(topic);
                ArrayList<QuestionBean> questions = new ArrayList<>();
                questions.add(bean);
                topic.setQuestions(questions);
            }
        }
        Collections.sort(topics, new Comparator<TopicBean>() {
            @Override
            public int compare(TopicBean o1, TopicBean o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        getAdapter().setNewInstance(topics);
    }
}