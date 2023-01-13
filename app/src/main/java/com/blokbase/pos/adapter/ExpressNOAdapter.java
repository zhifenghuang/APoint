package com.blokbase.pos.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.DistrictBean;
import com.common.lib.bean.ExpressNOBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExpressNOAdapter extends BaseQuickAdapter<ExpressNOBean, BaseViewHolder> {

    private Context mContext;

    public ExpressNOAdapter(Context context) {
        super(R.layout.item_express_no);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ExpressNOBean bean) {
        helper.setText(R.id.tvText, bean.getExpressName()+":"+bean.getExpressNo());
    }
}
