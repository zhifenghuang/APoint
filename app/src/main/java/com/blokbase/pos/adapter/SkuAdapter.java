package com.blokbase.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.GoodsSkuBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkuAdapter extends BaseQuickAdapter<GoodsSkuBean, BaseViewHolder> {

    private Context mContext;

    public SkuAdapter(Context context) {
        super(R.layout.item_sku);
        mContext = context;
    }


    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsSkuBean bean) {
        TextView tvText = helper.getView(R.id.tvText);
        tvText.setText(bean.getSku() + "");
        tvText.setTextColor(Color.parseColor(bean.isSelect() ? "#FFFFFF" : "#CC000000"));
        tvText.setBackgroundResource(bean.isSelect() ?
                R.drawable.shape_00a0e9_15 : R.drawable.shape_stroke_cc000000_15);
    }

    public void resetSelect(int position) {
        List<GoodsSkuBean> list = getData();
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            list.get(i).setSelect(i == position);
        }
        notifyDataSetChanged();
    }

}
