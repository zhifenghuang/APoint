package com.blokbase.pos.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.InviteAdapter;
import com.blokbase.pos.contract.InviteContract;
import com.blokbase.pos.presenter.InvitePresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.InviteBean;
import com.common.lib.bean.PosterBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.bean.UserBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.common.lib.utils.PermissionUtil;
import com.common.lib.utils.QRCodeUtil;

import java.util.ArrayList;

public class InviteActivity extends BaseActivity<InviteContract.Presenter> implements InviteContract.View {

    private Bitmap mBmp;
    private String mLink;

    private InviteAdapter mAdapter;
    private int mPageNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_invite);
        setViewsOnClickListener(R.id.tvUID, R.id.tvAwardRule, R.id.tvSave, R.id.tvCreateInviteLink);
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
//        GradeBean grade = myInfo.getGrade();
//        if (grade != null) {
//            setText(R.id.tvGrade, grade.getNameStr());
//            int drawableId = getResources().getIdentifier("app_node_" + grade.getId(), "drawable", getPackageName());
//            setImage(R.id.ivGrade, drawableId);
//        }
        setText(R.id.tvUID, "UID: " + myInfo.getUserId());
        setText(R.id.tvName, myInfo.getLoginAccount());
        getPresenter().poster();
        getPresenter().inviteDetail(1);
        resetUI();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        getAdapter().onAttachedToRecyclerView(recyclerView);
        recyclerView.setAdapter(getAdapter());
    }

    private InviteAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new InviteAdapter(this);
        }
        return mAdapter;
    }

    @NonNull
    @Override
    protected InviteContract.Presenter onCreatePresenter() {
        return new InvitePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUID:
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, myInfo.getUserId());
                break;
            case R.id.tvAwardRule:
                getPresenter().getAwardRule();
                break;
            case R.id.tvSave:
                if (mBmp == null) {
                    getPresenter().poster();
                    return;
                }
                if (!PermissionUtil.INSTANCE.isGrantPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermission(null, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    return;
                }
                BaseUtils.StaticParams.saveJpegToAlbum(mBmp, this);
                showToast(R.string.app_save_success);
                break;
            case R.id.tvCreateInviteLink:
                if (TextUtils.isEmpty(mLink)) {
                    getPresenter().poster();
                    return;
                }
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, mLink);
                break;
        }
    }

    private void resetUI() {
        PosterBean poster = DataManager.Companion.getInstance().getPoster();
        if (poster != null) {
            mLink = poster.getLink();
            mBmp = QRCodeUtil.createQRImage(this, mLink, null);
            setImage(R.id.ivQrCode, mBmp);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBmp != null && !mBmp.isRecycled()) {
            mBmp.recycle();
        }
        mBmp = null;
    }

    @Override
    public void getPosterSuccess(PosterBean poster) {
        DataManager.Companion.getInstance().savePoster(poster);
        resetUI();
    }

    @Override
    public void getInviteDetailSuccess(int pageIndex, ArrayList<InviteBean> list) {
        if (isFinish()) {
            return;
        }
        mPageNo = pageIndex;
        if (pageIndex == 1) {
            getAdapter().getData().clear();
            getAdapter().setNewInstance(list);
        } else {
            getAdapter().addData(list);
        }
        getAdapter().notifyDataSetChanged();
        int size = getAdapter().getItemCount();
        if (size > 0 && size % 50 == 0) {
            getPresenter().inviteDetail(pageIndex + 1);
        }
    }

    @Override
    public void getAwardRuleSuccess(QuestionBean bean) {
        if (isFinish()) {
            return;
        }
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_award_rule_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {
            @Override
            public void initView(@Nullable View view) {
                ((TextView) view.findViewById(R.id.tvTitle)).setText(bean.getTitleStr());
                ((TextView) view.findViewById(R.id.tvContent)).setText(Html.fromHtml(bean.getContentStr()));
                dialogFragment.setDialogViewsOnClickListener(view, R.id.ivClose);
            }

            @Override
            public void onViewClick(int viewId) {
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }
}
