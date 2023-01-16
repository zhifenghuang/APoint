package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.GoodsAdapter;
import com.blokbase.pos.contract.SearchContract;
import com.blokbase.pos.presenter.SearchPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.GoodsBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.common.lib.view.FlowLayout;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity<SearchContract.Presenter> implements SearchContract.View {

    private GoodsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
        setViewsOnClickListener(R.id.tvSearch, R.id.ivClear);
        resetFlowLayout();
    }

    private GoodsAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new GoodsAdapter(this);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_EXTRA, mAdapter.getItem(position));
                    openActivity(GoodsDetailActivity.class, bundle);
                }
            });
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected SearchContract.Presenter onCreatePresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClear:
                DataManager.Companion.getInstance().saveKeyWords(null);
                resetFlowLayout();
                break;
            case R.id.tvSearch:
                String text = getTextById(R.id.etSearch);
                if (TextUtils.isEmpty(text)) {
                    showToast(R.string.app_please_input_search_content);
                    return;
                }
                search(text);
                break;
        }
    }

    private void search(String text) {
        getPresenter().searchGoods(text);
        ArrayList<String> list = DataManager.Companion.getInstance().getKeyWords();
        if (list.contains(text)) {
            list.remove(text);
        }
        list.add(0, text);
        while (list.size() > 10) {
            list.remove(10);
        }
        DataManager.Companion.getInstance().saveKeyWords(list);
        resetFlowLayout();
    }

    private void resetFlowLayout() {
        FlowLayout layout = findViewById(R.id.flowLayout);
        layout.removeAllViews();
        ArrayList<String> list = DataManager.Companion.getInstance().getKeyWords();
        if (list.isEmpty()) {
            return;
        }
        int index = 0;
        for (String text : list) {
            View item = LayoutInflater.from(this).inflate(R.layout.item_search_text, null);
            ((TextView) item.findViewById(R.id.tvText)).setText(text);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = BaseUtils.StaticParams.dp2px(this, 8);
            lp.bottomMargin = BaseUtils.StaticParams.dp2px(this, 8);
            layout.addView(item, index, lp);
            ++index;
            item.setTag(text);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setText(R.id.etSearch, text);
                    search(text);
                }
            });
        }
    }

    @Override
    public void getGoodsListSuccess(ArrayList<GoodsBean> list) {
        if (isFinish()) {
            return;
        }
        setViewVisible(R.id.llGoods);
        getAdapter().setNewInstance(list);
        if (getAdapter().getItemCount() == 0) {
            setViewGone(R.id.recyclerView);
            setViewVisible(R.id.tvNoContent);
        } else {
            setViewVisible(R.id.recyclerView);
            setViewGone(R.id.tvNoContent);
        }
    }
}
