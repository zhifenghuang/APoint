package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.BannerViewHolder;
import com.blokbase.pos.adapter.SkuAdapter;
import com.blokbase.pos.contract.GoodsDetailContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.GoodsDetailPresenter;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.BannerBean;
import com.common.lib.bean.GoodsBean;
import com.common.lib.bean.GoodsSkuBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.constant.Constants;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.common.lib.view.banner.BannerView;
import com.common.lib.view.banner.HolderCreator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoodsDetailActivity extends BaseActivity<GoodsDetailContract.Presenter>
        implements GoodsDetailContract.View {

    private GoodsBean mGoods;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_goods_detail);

        Bundle bundle = getIntent().getExtras();
        mGoods = (GoodsBean) bundle.getSerializable(Constants.BUNDLE_EXTRA);
        getPresenter().goodsDetail(mGoods.getId());
        showImages(mGoods.getAlbum());
        setText(R.id.tvGoodsName, mGoods.getGoodsName());
        String[] price = mGoods.getSalePrice().split("\\.");
        setText(R.id.tvPrice1, price[0]);
        setText(R.id.tvPrice2, "." + price[1] + " " + getString(mGoods.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points));
        setText(R.id.tvNum, mGoods.getStockNum());
        setViewGone(R.id.tvBuyNow);
        setViewsOnClickListener(R.id.tvBuyNow);
    }

    private void showImages(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList<BannerBean> banners = new ArrayList<>();
        for (String pic : list) {
            BannerBean bean = new BannerBean();
            bean.setFile(pic);
            banners.add(bean);
        }
        BannerView bannerView = findViewById(R.id.bannerView);
        bannerView.setIndicatorVisible(true);
        bannerView.setPages(banners, new HolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        bannerView.start();
    }

    @NonNull
    @Override
    protected GoodsDetailContract.Presenter onCreatePresenter() {
        return new GoodsDetailPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyNow:
                if (TextUtils.isEmpty(mGoods.getContent())) {
                    getPresenter().goodsDetail(mGoods.getId());
                    return;
                }
                showSkuDialog();
                break;
        }
    }

    private void showSkuDialog() {
        MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_select_sku);
        dialogFragment.setClickDismiss(false);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {

            private SkuAdapter mAdapter;
            private String mOnePrice;
            private GoodsSkuBean mSkuBean;
            private int mNum;

            private SkuAdapter getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new SkuAdapter(GoodsDetailActivity.this);
                }
                return mAdapter;
            }

            @Override
            public void initView(View view) {
                BaseUtils.StaticParams.loadImage(GoodsDetailActivity.this, 0, mGoods.getCoverPic(), view.findViewById(R.id.ivGoods));
                ((TextView) view.findViewById(R.id.tvName)).setText(mGoods.getGoodsName() + "");
                ((TextView) view.findViewById(R.id.tvNum)).setText("1");
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivReduce, R.id.ivAdd, R.id.tvCancel, R.id.tvOk);
                mNum = 1;
                if (mGoods.getSku() == null || mGoods.getSku().isEmpty()) {
                    view.findViewById(R.id.recyclerView).setVisibility(View.GONE);
                    view.findViewById(R.id.tv).setVisibility(View.GONE);
                    mOnePrice = mGoods.getSalePrice();
                    mSkuBean = null;
                } else {
                    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(GoodsDetailActivity.this, 2);
                    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    getAdapter().onAttachedToRecyclerView(recyclerView);
                    recyclerView.setAdapter(getAdapter());
                    getAdapter().setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ad, @NonNull View view, int position) {
                            getAdapter().resetSelect(position);
                            mOnePrice = getAdapter().getItem(position).getSalePrice();
                            mSkuBean = getAdapter().getItem(position);
                        }
                    });
                    getAdapter().setNewInstance(mGoods.getSku());
                    getAdapter().resetSelect(0);
                    mOnePrice = mGoods.getSku().get(0).getSalePrice();
                    mSkuBean = mGoods.getSku().get(0);
                }
                if (mGoods.getGoodsType() == 10) {
                    view.findViewById(R.id.ivReduce).setVisibility(View.GONE);
                    view.findViewById(R.id.ivAdd).setVisibility(View.GONE);
                }
                resetPrice(view);
            }

            private void resetPrice(View view) {
                String[] price = Utils.removeZero(String.valueOf(Double.parseDouble(mOnePrice) * mNum + 0.00001)).split("\\.");
                ((TextView) view.findViewById(R.id.tvPrice1)).setText(price[0]);
                if (price.length == 1) {
                    ((TextView) view.findViewById(R.id.tvPrice2)).setText(".00 " + getString(mGoods.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points));
                } else {
                    ((TextView) view.findViewById(R.id.tvPrice2)).setText("." + price[1] + " " + getString(mGoods.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points));
                }
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.ivReduce:
                        calculateNum(false);
                        break;
                    case R.id.ivAdd:
                        calculateNum(true);
                        break;
                    case R.id.tvCancel:
                        dialogFragment.dismiss();
                        break;
                    case R.id.tvOk:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.BUNDLE_EXTRA, mGoods);
                        bundle.putSerializable(Constants.BUNDLE_EXTRA_2, mSkuBean);
                        bundle.putInt(Constants.BUNDLE_EXTRA_3, mNum);
                        openActivity(OrderPayActivity.class, bundle);
                        dialogFragment.dismiss();
                        break;
                }
            }

            private void calculateNum(boolean isAdd) {
                try {
                    TextView tv = dialogFragment.getView().findViewById(R.id.tvNum);
                    if (isAdd) {
                        ++mNum;
                        setText(tv, String.valueOf(mNum));
                    } else {
                        --mNum;
                        if (mNum < 1) {
                            mNum = 1;
                        }
                        setText(tv, String.valueOf(mNum));
                    }
                    resetPrice(dialogFragment.getView());
                } catch (Exception e) {

                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    @Override
    public void getGoodsDetailSuccess(GoodsBean bean) {
        if (isFinish()) {
            return;
        }
        setViewVisible(R.id.tvBuyNow);
        mGoods = bean;
        setHtml(R.id.tvContent, bean.getContent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.equals(EventBusEvent.REFRESH_ORDER_LIST)) {
            finish();
        }
    }
}
