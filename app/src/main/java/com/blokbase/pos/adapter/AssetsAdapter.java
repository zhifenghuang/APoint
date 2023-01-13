package com.blokbase.pos.adapter;

import android.content.Context;
import android.util.Log;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.AssetsBean;

import org.jetbrains.annotations.NotNull;

public class AssetsAdapter extends BaseQuickAdapter<AssetsBean, BaseViewHolder> {

    private Context mContext;

    public AssetsAdapter(Context context) {
        super(R.layout.item_assets);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, AssetsBean bean) {
        int drawableId = 0;
        try {
            drawableId = mContext.getResources()
                    .getIdentifier("app_assets_" + bean.getSymbol().toLowerCase(), "drawable",
                            mContext.getPackageName());
        } catch (Exception e) {
        }
        String toUsdt = bean.getBalance();
        helper.setImageResource(R.id.ivLogo, drawableId)
                .setText(R.id.tvSymbol, bean.getSymbol2())
                .setGone(R.id.tvToUsdt, bean.getSymbol().equalsIgnoreCase("INTEGRAL"))
                .setText(R.id.tvBalance, Utils.removeZero(bean.getBalance()))
                .setText(R.id.tvToUsdt, "≈ $" + Utils.removeZero(toUsdt));
    }

}
