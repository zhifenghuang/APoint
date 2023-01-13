package com.blokbase.pos.adapter;

import android.content.Context;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.OrderBean;
import com.common.lib.utils.BaseUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {

    private Context mContext;

    public OrderAdapter(Context context) {
        super(R.layout.item_order);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderBean bean) {
        HashMap<String, Object> goods = bean.getGoods().get(0);
        BaseUtils.StaticParams.loadImage(mContext, 0, (String) goods.get("coverPic"), helper.getView(R.id.ivGoods));

        String status = "";
        try {
            status = mContext.getString(mContext.getResources()
                    .getIdentifier("app_order_status_" + bean.getDeliveryStatus(), "string", mContext.getPackageName()));
        } catch (Exception e) {

        }
        String unit = mContext.getString(R.string.app_a_points);
        String[] price = bean.getPayAmount().split("\\.");
        String sku = (String) goods.get("sku");
        helper.setText(R.id.tvPayTime, mContext.getString(R.string.app_pay_time) + bean.getCreateTime())
                .setText(R.id.tvName1, (String) goods.get("goodsName"))
                .setText(R.id.tvName2, sku == null ? "" : sku)
                .setText(R.id.tvNum, "x" + goods.get("buyNum").toString().split("\\.")[0])
                .setText(R.id.tvPrice1, price[0])
                .setText(R.id.tvPrice2, (price.length == 1 ? ".00" : "." + price[1]) + " " + unit)
                .setText(R.id.tvStatus, status)
                .setGone(R.id.ll, (bean.getDeliveryStatus() != 20 && bean.getDeliveryStatus() != 30))
                .setGone(R.id.tvSureReceive, bean.getDeliveryStatus() == 30);
    }

}
