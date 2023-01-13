package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.GoodsBean;
import com.common.lib.utils.BaseUtils;

import org.jetbrains.annotations.NotNull;

public class GoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    private Context mContext;

    public GoodsAdapter(Context context) {
        super(R.layout.item_goods);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsBean bean) {
        BaseUtils.StaticParams.loadImage(mContext, 0, bean.getCoverPic(), helper.getView(R.id.ivGoods));
        String unit = mContext.getString(bean.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points);
        String[] price = bean.getSalePrice().split("\\.");
        helper.setText(R.id.tvName, bean.getGoodsName())
                .setText(R.id.tvPrice1, price[0])
                .setText(R.id.tvPrice2, (price.length == 1 ? ".00 " : "." + price[1] + " ") + unit);
    }

}
