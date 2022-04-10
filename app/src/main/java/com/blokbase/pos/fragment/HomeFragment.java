package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.CalculatorActivity;
import com.blokbase.pos.activity.ChainDataActivity;
import com.blokbase.pos.activity.MainActivity;
import com.blokbase.pos.activity.MineActivity;
import com.blokbase.pos.activity.NoticeListActivity;
import com.blokbase.pos.activity.ObserverActivity;
import com.blokbase.pos.activity.PoolNodeRankActivity;
import com.blokbase.pos.adapter.BannerViewHolder;
import com.blokbase.pos.adapter.TextItemViewDelegate;
import com.blokbase.pos.contract.HomeContract;
import com.blokbase.pos.presenter.HomePresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.bean.BannerBean;
import com.common.lib.bean.ChainDataBean;
import com.common.lib.bean.ChainNodeBean;
import com.common.lib.bean.HomeDataBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.bean.QuotationsBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.view.banner.BannerView;
import com.common.lib.view.banner.HolderCreator;
import com.xj.marqueeview.MarqueeView;
import com.xj.marqueeview.base.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View, View.OnTouchListener {

    @NonNull
    @Override
    protected HomeContract.Presenter onCreatePresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        setViewsOnClickListener(R.id.ivMore, R.id.ivProfile, R.id.llObserver, R.id.llCalculator, R.id.llChainData, R.id.llRank);
        getPresenter().homeData();
        showNotice(DataManager.Companion.getInstance().getNoticeList());
        showBanners(DataManager.Companion.getInstance().getBanners());
        showHomePosData(DataManager.Companion.getInstance().getHomePosData());
        setText(R.id.tvPrice, DataManager.Companion.getInstance().getUtgPrice());
        view.findViewById(R.id.llObserver).setOnTouchListener(this);
        view.findViewById(R.id.llCalculator).setOnTouchListener(this);
        view.findViewById(R.id.llChainData).setOnTouchListener(this);
        view.findViewById(R.id.llRank).setOnTouchListener(this);
    }

    public void getNoticeBanner() {
        if (getView() == null) {
            return;
        }
        getPresenter().bannerList();
        getPresenter().noticeList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMore:
            case R.id.ivProfile:
                openActivity(MineActivity.class);
                break;
            case R.id.llObserver:
                openActivity(ObserverActivity.class);
                break;
            case R.id.llCalculator:
                if (DataManager.Companion.getInstance().getHomePosData() == null) {
                    getPresenter().homeData();
                    return;
                }
                openActivity(CalculatorActivity.class);
                break;
            case R.id.llChainData:
                openActivity(ChainDataActivity.class);
                break;
            case R.id.llRank:
                openActivity(PoolNodeRankActivity.class);
                break;
        }
    }

    public void showUtgPrice(QuotationsBean bean) {
        if (getView() == null) {
            return;
        }
        setText(R.id.tvPrice, bean.getLast());
        setImage(R.id.ivRose, bean.getChange() > 0 ? R.drawable.app_grow_up : R.drawable.app_grow_down);
    }

    @Override
    public void getBannerListSuccess(ArrayList<BannerBean> list) {
        DataManager.Companion.getInstance().saveBanners(list);
        showBanners(list);
    }

    private void showBanners(ArrayList<BannerBean> list) {
        if (getView() == null) {
            return;
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        BannerView bannerView = getView().findViewById(R.id.bannerView);
        bannerView.setIndicatorVisible(true);
        bannerView.setPages(list, new HolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        bannerView.start();
    }

    @Override
    public void getNoticeListSuccess(ArrayList<NoticeBean> list) {
        if (getView() == null || list == null || list.isEmpty()) {
            return;
        }
        showNotice(list);
        ((MainActivity) getActivity()).showNoticeDialog(list.get(0));
    }

    @Override
    public void getHomePosDataSuccess(HomeDataBean bean) {
        showHomePosData(bean);
        HashMap<String, Object> map = new HashMap<>();
        map.put(EventBusEvent.REFRESH_PLEDGE_UI, "");
        EventBus.getDefault().post(map);
    }

    private void showHomePosData(HomeDataBean bean) {
        if (getView() == null || bean == null) {
            return;
        }
        setText(R.id.tvTotalPledge, Utils.removeZero(bean.getUltronNode()));
        setText(R.id.tvMyPledge, Utils.removeZero(bean.getPledgeAmount()));
        try {
            setText(R.id.tvMyPercent, new BigDecimal(bean.getPledgeAmount())
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(bean.getUltronNode()), 2, RoundingMode.HALF_UP) + "%");
        } catch (Exception e) {
            setText(R.id.tvMyPercent, "0%");
        }
        setText(R.id.tvTotalProfit, Utils.removeZero(bean.getTotalProfit()));
        setText(R.id.tvMyTotalProfit, Utils.removeZero(bean.getMyTotalProfit()));
        setText(R.id.tvMyTodayProfit, Utils.removeZero(bean.getMyYesterdayProfit()));
        setText(R.id.tvPoolTodayProfit, Utils.removeZero(bean.getYesterdayProfit()));

    }

    private void showNotice(ArrayList<NoticeBean> list) {
        if (getView() == null) {
            return;
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        final MarqueeView marqueeView = getView().findViewById(R.id.marqueeView);
        MultiItemTypeAdapter<NoticeBean> multiItemTypeAdapter = new MultiItemTypeAdapter<NoticeBean>(getActivity(), list);
        multiItemTypeAdapter.addItemViewDelegate(new TextItemViewDelegate());
        multiItemTypeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (!marqueeView.isStart()) {
                    marqueeView.startFlip();
                }
                openActivity(NoticeListActivity.class);
            }
        });
        marqueeView.setAdapter(multiItemTypeAdapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LinearLayout ll = (LinearLayout) v;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ll.getId() == R.id.llObserver) {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_observer_on);
                } else if (ll.getId() == R.id.llCalculator) {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_calculator_on);
                } else if (ll.getId() == R.id.llChainData) {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_chain_data_on);
                } else {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_node_on);
                }
                ll.setBackgroundResource(R.drawable.shape_6961f3_9);
                ((TextView) ll.getChildAt(1)).setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_3));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (ll.getId() == R.id.llObserver) {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_observer_off);
                } else if (ll.getId() == R.id.llCalculator) {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_calculator_off);
                } else if (ll.getId() == R.id.llChainData) {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_chain_data_off);
                } else {
                    ((ImageView) ll.getChildAt(0)).setImageResource(R.drawable.app_node_off);
                }
                ll.setBackgroundResource(0);
                ((TextView) ll.getChildAt(1)).setTextColor(ContextCompat.getColor(getActivity(), R.color.text_color_1));
                break;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (getView() == null) {
            return;
        }
        if (map.containsKey(EventBusEvent.UPDATE_PLEDGE_INFO)) {
            getPresenter().homeData();
        }
    }
}
