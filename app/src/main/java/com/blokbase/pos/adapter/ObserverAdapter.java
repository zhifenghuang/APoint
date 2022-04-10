package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ObserverBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ObserverAdapter extends BaseQuickAdapter<ObserverBean, BaseViewHolder> {

    private Context mContext;
    private boolean mIsEdit;

    public ObserverAdapter(Context context) {
        super(R.layout.item_observer);
        mContext = context;
        mIsEdit = false;
    }

    public void setEdit(boolean isEdit) {
        mIsEdit = isEdit;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ObserverBean bean) {
        helper.setText(R.id.tvRemark, bean.getRemark())
                .setText(R.id.tvAccount, bean.getAccount())
                .setImageResource(R.id.iv, mIsEdit ? R.drawable.app_delete : R.drawable.app_arrow_right_black);
    }

    public void deleteById(String id) {
        List<ObserverBean> list = getData();
        for (ObserverBean bean : list) {
            if (id.equals(bean.getId())) {
                remove(bean);
                notifyDataSetChanged();
                break;
            }
        }
    }
}
