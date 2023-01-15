package com.blokbase.pos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.GoodsDetailActivity;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.GoodsBean;
import com.common.lib.constant.Constants;
import com.common.lib.utils.BaseUtils;
import com.common.lib.view.banner.ViewHolder;

public class GoodsBannerViewHolder implements ViewHolder<GoodsBean> {
    private Context mContext;
    private View view;

    @Override
    public View createView(Context context) {
        mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.item_package_goods, null);
        return view;
    }


    @Override
    public void onBind(Context context, int position, GoodsBean bean) {
        BaseUtils.StaticParams.loadImage(mContext, 0, bean.getCoverPic(), view.findViewById(R.id.ivGoods));
        ((TextView) view.findViewById(R.id.tvName)).setText(bean.getGoodsName());
        String unit = mContext.getString(bean.getGoodsType() == 20 ? R.string.app_uaa : R.string.app_a_points);
        String[] price = bean.getSalePrice().split("\\.");
        ((TextView) view.findViewById(R.id.tvPrice1)).setText(price[0]);
        ((TextView) view.findViewById(R.id.tvPrice2)).setText((price.length == 1 ? ".00 " : "." + price[1] + " ") + unit);

        TextView tvBuy = view.findViewById(R.id.tvBuy);
        tvBuy.setTag(bean);
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsBean goodsBean = (GoodsBean) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, goodsBean);
                ((BaseActivity) mContext).openActivity(GoodsDetailActivity.class, bundle);
            }
        });
    }
}
