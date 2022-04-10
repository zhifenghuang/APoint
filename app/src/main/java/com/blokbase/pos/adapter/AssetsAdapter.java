package com.blokbase.pos.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.AssetsBean;
import com.common.lib.manager.DataManager;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class AssetsAdapter extends BaseQuickAdapter<AssetsBean, BaseViewHolder> {

    private Context mContext;
    private boolean mIsShowAssets;

    public AssetsAdapter(Context context) {
        super(R.layout.item_assets);
        mContext = context;
    }

    public void setShowAssets(boolean isShowAssets) {
        mIsShowAssets = isShowAssets;
        notifyDataSetChanged();
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
        helper.setImageResource(R.id.ivLogo, drawableId)
                .setText(R.id.tvSymbol, bean.getSymbol());
        if (mIsShowAssets) {
            String symbol = bean.getSymbol();
            String toUsdt = bean.getBalance();
            BigDecimal balance = new BigDecimal(Utils.removeZero(bean.getBalance()));
            if (!balance.equals(BigDecimal.ZERO)) {
                if (symbol.equalsIgnoreCase("UTG")) {
                    String price = DataManager.Companion.getInstance().getUtgPrice();
                    toUsdt = balance.multiply(new BigDecimal(price)).toString();
                }
            }
            helper.setText(R.id.tvFreeze, mContext.getString(R.string.app_freeze_xxx, Utils.removeZero(bean.getFreeze())))
                    .setText(R.id.tvBalance, Utils.removeZero(bean.getBalance()))
                    .setText(R.id.tvToUsdt, "≈ $" + Utils.removeZero(toUsdt));
        } else {
            helper.setText(R.id.tvFreeze, "******")
                    .setText(R.id.tvBalance, "******")
                    .setText(R.id.tvToUsdt, "≈ $******");
        }
    }
}
