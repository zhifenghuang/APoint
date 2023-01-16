package com.blokbase.pos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.R;
import com.blokbase.pos.contract.MainContract;
import com.blokbase.pos.fragment.AssetsFragment;
import com.blokbase.pos.fragment.HomeFragment;
import com.blokbase.pos.fragment.MineFragment;
import com.blokbase.pos.fragment.OrdersFragment;
import com.blokbase.pos.fragment.UAASwapGoodsFragment;
import com.blokbase.pos.presenter.MainPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.BannerBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.constant.EventBusEvent;
import com.common.lib.dialog.AppUpgradeDialog;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.manager.MediaplayerManager;
import com.common.lib.utils.BaseUtils;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    private ArrayList<BaseFragment> mBaseFragment;

    private int mCurrentItem;
    private MyHandler mHandler;
    private AppUpgradeDialog mAppUpgradeDialog;
    private boolean mIsActivityPause;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        initFragments();
        initViews();
        switchFragment(mBaseFragment.get(0));
        mHandler = new MyHandler(this);
        mHandler.sendEmptyMessageDelayed(0, 1000);
        mHandler.sendEmptyMessageDelayed(1, 2000);
        mHandler.sendEmptyMessageDelayed(2, 3000);
        changeRefreshLanguage();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPause = false;
        getPresenter().assetsList();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPause = true;
    }

    @Override
    public void checkVersionSuccess(VersionBean bean) {
        if (bean.getVersionCode() > BuildConfig.VERSION_CODE && !mIsActivityPause) {
            mAppUpgradeDialog = new AppUpgradeDialog(this, bean);
            mAppUpgradeDialog.show();
        }
    }

    @Override
    public void getBannerListSuccess(ArrayList<BannerBean> list) {
        if (isFinish() || mBaseFragment == null || mBaseFragment.isEmpty()) {
            return;
        }
        ((HomeFragment) mBaseFragment.get(0)).showBanners(list);
    }

    @Override
    public void getNoticeListSuccess(ArrayList<NoticeBean> list) {
        if (isFinish() || mBaseFragment == null || mBaseFragment.isEmpty()) {
            return;
        }
        ((HomeFragment) mBaseFragment.get(0)).showNotice(list);
    }

    @Override
    public void getAssetsListSuccess(ArrayList<AssetsBean> list) {
        DataManager.Companion.getInstance().saveAssets(list);
        if (mBaseFragment == null || mBaseFragment.size() < 4) {
            return;
        }
        ((AssetsFragment) mBaseFragment.get(3)).showAssets();
    }

    private void initFragments() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new HomeFragment());
        mBaseFragment.add(new UAASwapGoodsFragment());
        mBaseFragment.add(new OrdersFragment());
        mBaseFragment.add(new AssetsFragment());
        mBaseFragment.add(new MineFragment());
    }

    private void initViews() {
        LinearLayout llBottom = findViewById(R.id.llBottom);
        int count = llBottom.getChildCount();
        View itemView;
        for (int i = 0; i < count; ++i) {
            itemView = llBottom.getChildAt(i);
            itemView.setTag(i);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (tag == mCurrentItem) {
                        return;
                    }
                    mBaseFragment.get(tag).onRefresh();
                    switchFragment(mBaseFragment.get(tag));
                    resetBottomBar(tag);
                }
            });
        }
    }

    private void resetBottomBar(int currentPos) {
        mCurrentItem = currentPos;
        LinearLayout llBottom = findViewById(R.id.llBottom);
        int count = llBottom.getChildCount();
        ViewGroup itemView;
        for (int i = 0; i < count; ++i) {
            itemView = (ViewGroup) llBottom.getChildAt(i);
            (((ImageView) itemView.getChildAt(0))).setImageResource(getResIdByIndex(i, currentPos == i));
            (((TextView) itemView.getChildAt(1))).setTextColor(ContextCompat.getColor(this, currentPos == i ? R.color.text_color_5 : R.color.text_color_2));
        }
    }

    private int getResIdByIndex(int index, boolean isCheck) {
        int id = 0;
        switch (index) {
            case 0:
                id = isCheck ? R.drawable.app_home_on : R.drawable.app_home_off;
                break;
            case 1:
                id = isCheck ? R.drawable.app_swap_on : R.drawable.app_swap_off;
                break;
            case 2:
                id = isCheck ? R.drawable.app_order_on : R.drawable.app_order_off;
                break;
            case 3:
                id = isCheck ? R.drawable.app_assets_on : R.drawable.app_assets_off;
                break;
            case 4:
                id = isCheck ? R.drawable.app_me_on : R.drawable.app_me_off;
                break;
        }
        return id;
    }

    @NonNull
    @Override
    protected MainContract.Presenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }

    public int getContainerViewId() {
        return R.id.fl;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAppUpgradeDialog != null) {
            mAppUpgradeDialog.dismiss();
        }
        mAppUpgradeDialog = null;
        MediaplayerManager.getInstance().releaseSoundPool();
        mHandler.setMainActivityNull();
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        mHandler.removeMessages(2);
        mHandler = null;
    }


    public static class MyHandler extends Handler {

        private MainActivity mainActivity;

        public MyHandler(MainActivity activity) {
            mainActivity = activity;
        }

        public void setMainActivityNull() {
            mainActivity = null;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mainActivity == null || mainActivity.isFinish()) {
                return;
            }
            switch (msg.what) {
                case 0:
                    mainActivity.getBannerList();
                    break;
                case 1:
                    mainActivity.getNoticeList();
                    break;
                case 2:
                    mainActivity.getPresenter().checkVersion();
                    break;

            }
        }
    }

    public void getBannerList() {
        getPresenter().bannerList();
    }

    public void getNoticeList() {
        getPresenter().noticeList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceive(HashMap<String, Object> map) {
        if (map.containsKey(EventBusEvent.REFRESH_ASSETS) ||
                map.containsKey(EventBusEvent.UPDATE_PLEDGE_INFO)) {
            getPresenter().assetsList();
        } else {
            super.onReceive(map);
        }
    }

    public void showNoticeDialog(final NoticeBean bean) {
        if (mIsActivityPause) {
            return;
        }
        BaseUtils.StaticParams.changeAppLanguage(this, DataManager.Companion.getInstance().getLanguage());
        MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_notice_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                ((TextView) view.findViewById(R.id.tvTitle)).setText(bean.getTitleStr());
                WebView webView = view.findViewById(R.id.webView);
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webView.loadDataWithBaseURL(null, bean.getContentStr(), "text/html", "utf-8", null);
                //  ((TextView) view.findViewById(R.id.tvDetail)).setText(getString(R.string.app_look_detail));
                dialogFragment.setDialogViewsOnClickListener(view, R.id.tvDetail, R.id.ivClose);
            }

            @Override
            public void onViewClick(int viewId) {
                if (viewId == R.id.tvDetail) {

                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    private void changeRefreshLanguage() {
        if (DataManager.Companion.getInstance().getLanguage() == 0) {
            ClassicsHeader.REFRESH_HEADER_PULLDOWN = "Pull down to refresh";
            ClassicsHeader.REFRESH_HEADER_REFRESHING = "Refreshing...";
            ClassicsHeader.REFRESH_HEADER_LOADING = "Loading...";
            ClassicsHeader.REFRESH_HEADER_RELEASE = "Refresh after release";
            ClassicsHeader.REFRESH_HEADER_FINISH = "Refresh complete";
            ClassicsHeader.REFRESH_HEADER_FAILED = "Refresh failed";
            ClassicsHeader.REFRESH_HEADER_LASTTIME = "'Last refresh' M-d HH:mm";
            ClassicsHeader.REFRESH_HEADER_SECOND_FLOOR = "Second floor";
        } else {
            ClassicsHeader.REFRESH_HEADER_PULLDOWN = "下拉可以刷新";
            ClassicsHeader.REFRESH_HEADER_REFRESHING = "正在刷新...";
            ClassicsHeader.REFRESH_HEADER_LOADING = "正在加载...";
            ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
            ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
            ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
            ClassicsHeader.REFRESH_HEADER_LASTTIME = "上次更新 M-d HH:mm";
            ClassicsHeader.REFRESH_HEADER_SECOND_FLOOR = "释放进入二楼";
        }
    }
}