package com.blokbase.pos.adapter;

import android.content.Context;
import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.ExpressNOBean;
import org.jetbrains.annotations.NotNull;

public class ExpressNOAdapter extends BaseQuickAdapter<ExpressNOBean, BaseViewHolder> {

    private Context mContext;
    private int mSelectPos;

    public ExpressNOAdapter(Context context) {
        super(R.layout.item_express_no);
        mContext = context;
        mSelectPos = 0;
    }

    public void setSelectPos(int selectPos) {
        mSelectPos = selectPos;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ExpressNOBean bean) {
        helper.setText(R.id.tvText, bean.getExpressName() + ":" + bean.getExpressNo())
                .setBackgroundResource(R.id.ll, mSelectPos == getItemPosition(bean) ?
                        R.drawable.shape_stroke_ed282a_7 : R.drawable.shape_stroke_e4e5e7_7);
    }
}
