package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.QuestionBean;
import com.common.lib.bean.UserBean;
import com.common.lib.manager.DataManager;

import org.jetbrains.annotations.NotNull;

public class AccountAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    private Context mContext;
    private UserBean mCurrentUser;

    public AccountAdapter(Context context) {
        super(R.layout.item_account);
        mContext = context;
        mCurrentUser = DataManager.Companion.getInstance().getMyInfo();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, UserBean bean) {
        helper.setText(R.id.tvAccount, bean.getLoginAccount())
                .setVisible(R.id.ivCheck, bean.getUserId().equals(mCurrentUser.getUserId()));
    }
}
