package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.CheckInContract;
import com.blokbase.pos.presenter.CheckInPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.CheckInBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.manager.MediaplayerManager;
import com.common.lib.utils.BaseUtils;

public class CheckInActivity extends BaseActivity<CheckInContract.Presenter> implements CheckInContract.View {

    private boolean mIsActivityPause;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_in;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_check_in_center);
        setTextColor(R.id.tvTitle, R.color.text_color_3);
        setImage(R.id.ivBack, R.drawable.app_back_white);
        checkInOverviewSuccess(DataManager.Companion.getInstance().getCheckInBean());

        MediaplayerManager.getInstance().loadSound(this, R.raw.gold_coin);
        setViewsOnClickListener(R.id.tvCheckIn);
        getPresenter().checkInOverview();

        LinearLayout llDays = findViewById(R.id.llDays);
        int count = llDays.getChildCount();
        for (int i = 0; i < count; ++i) {
            ((TextView) llDays.getChildAt(i)).setText(getString(R.string.app_xxx_day, String.valueOf(i + 1)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPause = true;
    }

    @NonNull
    @Override
    protected CheckInContract.Presenter onCreatePresenter() {
        return new CheckInPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCheckIn:
                getPresenter().checkInSubmit();
                break;
        }
    }


    @Override
    public void checkInOverviewSuccess(CheckInBean bean) {
        if (isFinish()) {
            return;
        }
        if (bean == null) {
            bean = new CheckInBean();
            bean.setCheckIn(false);
            bean.setCount(0);
        }
        setHtml(R.id.tvCheckInDays, getString(R.string.app_had_continue_mark_xxx_day, "<font color=\"#ED4C4A\">" + bean.getCount() + "</font>"));
        TextView tvCheckIn = findViewById(R.id.tvCheckIn);
        if (bean.getCheckIn()) {
            tvCheckIn.setAlpha(0.6f);
            tvCheckIn.setEnabled(false);
        } else {
            tvCheckIn.setAlpha(1.0f);
            tvCheckIn.setEnabled(true);
        }

        LinearLayout llDays = findViewById(R.id.llDays);
        int count = llDays.getChildCount();
        for (int i = 0; i < count; ++i) {
            ((TextView) llDays.getChildAt(i)).setTextColor(
                    ContextCompat.getColor(this, i < bean.getCount() ? R.color.text_color_1 : R.color.color_ba_ba_ba));
        }

        LinearLayout llAwards = findViewById(R.id.llAwards);
        count = llAwards.getChildCount();
        for (int i = 0; i < count; ++i) {
            llAwards.getChildAt(i).setAlpha(2 * i + 1 <= bean.getCount() ? 1.0f : 0.5f);
        }
        int dx = (int) (BaseUtils.StaticParams.dp2px(this, 312) * Math.min(bean.getCount(), 7) / 7 + 0.5);
        View progressView = findViewById(R.id.progress_view);
        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) progressView.getLayoutParams();
        lp1.width = dx;
        progressView.setLayoutParams(lp1);
    }

    @Override
    public void checkInSubmitSuccess() {
        if (isFinish()) {
            return;
        }
        showToast(R.string.app_mark_success);
        MediaplayerManager.getInstance().playSound(getContext(), R.raw.gold_coin);
        findViewById(R.id.tvCheckIn).postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaplayerManager.getInstance().releaseSoundPool();
            }
        }, 1100);
        CheckInBean bean = DataManager.Companion.getInstance().getCheckInBean();
        if (bean == null) {
            getPresenter().checkInOverview();
        } else {
            bean.setCount(bean.getCount() + 1);
            bean.setCheckIn(true);
            DataManager.Companion.getInstance().saveCheckInBean(bean);
            checkInOverviewSuccess(bean);
            showCheckInSuccessDialog(bean.getCount());
        }
    }

    private void showCheckInSuccessDialog(final int count) {
        if (mIsActivityPause) {
            return;
        }
        MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_check_in_award_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(View view) {
                ((TextView) view.findViewById(R.id.tvCheckInDays)).setText(getString(R.string.app_had_continue_mark_xxx_day, String.valueOf(count)));
                String uaa = "0.1";
                switch (count) {
                    case 1:
                        uaa = "0.05";
                        break;
                    case 2:
                        uaa = "0.06";
                        break;
                    case 3:
                        uaa = "0.07";
                        break;
                    case 4:
                        uaa = "0.08";
                        break;
                    case 5:
                        uaa = "0.09";
                        break;
                    default:
                        uaa = "0.1";
                        break;
                }
                ((TextView) view.findViewById(R.id.tvGetUaa)).
                        setText(Html.fromHtml(getString(R.string.app_congratulate_you_get_xxx_uaa, "<font color=\"#ED4C4A\">" + uaa + "</font>")));

                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose);
            }

            @Override
            public void onViewClick(int viewId) {
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }

    public void onDestroy() {
        super.onDestroy();
        MediaplayerManager.getInstance().releaseSoundPool();
    }
}
