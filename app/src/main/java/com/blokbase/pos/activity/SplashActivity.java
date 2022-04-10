package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.PrefUtil;

public class SplashActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
//        getWindow().getDecorView().setBackgroundResource(DataManager.Companion.getInstance().getLanguage() == 0 ?
//                R.drawable.app_splash_en : R.drawable.app_splash);
//        getSystemInfo(false);


        boolean isAgree = PrefUtil.getBoolean(this, "is_agree", false);
        if (isAgree) {
            next();
        } else {
            showAgreementDialog();
        }
    }

    private void next() {
//                long delayTime = 2000;
//        String splashUrl = DataManager.Companion.getInstance().getSplashUrl();
//        if (!TextUtils.isEmpty(splashUrl) && splashUrl.startsWith("http")) {
//            BaseUtils.StaticParams.loadImage(this, 0, splashUrl, findViewById(R.id.iv));
//        } else {
//            delayTime = 2000;
//        }
        findViewById(R.id.iv).postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(DataManager.Companion.getInstance().getMyInfo() == null ?
                        LoginActivity.class : MainActivity.class);
                finish();
            }
        }, 1500);
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    private boolean mIsAgree;

    private void showAgreementDialog() {
        mIsAgree = false;
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_risk_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(@Nullable View view) {
                TextView tvContent = view.findViewById(R.id.tvContent);
                tvContent.setText(Html.fromHtml(getString(R.string.app_risk_content, "<b><tt>", "</tt></b>")));

                TextView tvOk = view.findViewById(R.id.tvOk);
                tvOk.setEnabled(false);
                dialogFragment.setIsCanTouchDismiss(false);
                dialogFragment.setClickDismiss(false);
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivAgree, R.id.tvOk);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.ivAgree:
                        mIsAgree = !mIsAgree;
                        ((ImageView) dialogFragment.getView().findViewById(R.id.ivAgree))
                                .setImageResource(mIsAgree ? R.drawable.app_choose : R.drawable.app_not_choose);
                        TextView tvOk = dialogFragment.getView().findViewById(R.id.tvOk);
                        if (mIsAgree) {
                            tvOk.setBackgroundResource(R.drawable.shape_6961f3_9);
                            tvOk.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.text_color_3));
                            tvOk.setEnabled(true);
                        } else {
                            tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
                            tvOk.setTextColor(ContextCompat.getColor(SplashActivity.this, R.color.text_color_4));
                            tvOk.setEnabled(false);
                        }
                        break;
                    case R.id.tvOk:
                        PrefUtil.putBoolean(SplashActivity.this, "is_agree", true);
                        dialogFragment.dismiss();
                        openActivity(DataManager.Companion.getInstance().getMyInfo() == null ?
                                LoginActivity.class : MainActivity.class);
                        finish();
                        break;
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }
}
