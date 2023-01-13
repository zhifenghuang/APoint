package com.blokbase.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.CityAdapter;
import com.blokbase.pos.contract.SelectCityContract;
import com.blokbase.pos.presenter.SelectCityPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.DistrictBean;
import com.common.lib.constant.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends BaseActivity<SelectCityContract.Presenter>
        implements SelectCityContract.View {

    protected List<CityAdapter> mAdapters;

    private DistrictBean mSelectP, mSelectC, mSelectD;
    private int select;//0选择代理区 1选择代理市 2选择代理省 3选择收货地址

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_city);

        select = getIntent().getIntExtra(Constants.BUNDLE_EXTRA, 0);

        if (select == 1) {
            setViewGone(R.id.line2, R.id.rv3);
        } else if (select == 2) {
            setViewGone(R.id.line2, R.id.rv2, R.id.line2, R.id.rv3);
        }

        mAdapters = new ArrayList<>();
        for (int i = 1; i <= 3; ++i) {
            RecyclerView recyclerView = findViewById(getResources()
                    .getIdentifier("rv" + i, "id", getPackageName()));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            CityAdapter adapter = new CityAdapter(this);
            mAdapters.add(adapter);
            adapter.onAttachedToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);
            if (i == 1) {
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                        mSelectP = ((CityAdapter) ad).getItem(position);
                        if (select == 2) {  //选择省
                            Intent intent = new Intent();
                            intent.putExtra(Constants.BUNDLE_EXTRA, mSelectP);
                            intent.putExtra(Constants.BUNDLE_EXTRA_2, mSelectP.getText());
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            ((CityAdapter) ad).showSelect(position);
                            getPresenter().districtList(1, mSelectP.getValue());
                        }
                    }
                });
            } else if (i == 2) {
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                        mSelectC = ((CityAdapter) ad).getItem(position);
                        if (select == 1) {  //选择市
                            Intent intent = new Intent();
                            intent.putExtra(Constants.BUNDLE_EXTRA, mSelectC);
                            intent.putExtra(Constants.BUNDLE_EXTRA_2, mSelectP.getText() + "," + mSelectC.getText());
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            ((CityAdapter) ad).showSelect(position);
                            getPresenter().districtList(2, mSelectC.getValue());
                        }
                    }
                });
            } else {
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                        mSelectD = ((CityAdapter) ad).getItem(position);
                        ((CityAdapter) ad).showSelect(position);
                        Intent intent = new Intent();
                        if (select == 0) {
                            intent.putExtra(Constants.BUNDLE_EXTRA, mSelectD);
                            intent.putExtra(Constants.BUNDLE_EXTRA_2, mSelectP.getText() + "," + mSelectC.getText() + "," + mSelectD.getText());
                        } else {
                            intent.putExtra(Constants.BUNDLE_EXTRA, mSelectP);
                            intent.putExtra(Constants.BUNDLE_EXTRA_2, mSelectC);
                            intent.putExtra(Constants.BUNDLE_EXTRA_3, mSelectD);
                        }
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }
        getPresenter().districtList(0, "0");
    }


    @NonNull
    @Override
    protected SelectCityContract.Presenter onCreatePresenter() {
        return new SelectCityPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void getDistrictListSuccess(int index, String pCode, ArrayList<DistrictBean> list) {
        if (isFinish()) {
            return;
        }
        if (select < 3) {  //要把已被代理的区域排除
            for (int i = 0; i < list.size(); ) {
                if (list.get(i).getUserId() != null && list.get(i).getUserId() > 0) {
                    list.remove(i);
                    continue;
                }
                ++i;
            }
        }
        mAdapters.get(index).setNewInstance(list);
    }
}
