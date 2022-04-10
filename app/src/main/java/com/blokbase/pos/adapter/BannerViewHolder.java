package com.blokbase.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blokbase.pos.R;
import com.common.lib.bean.BannerBean;
import com.common.lib.utils.BaseUtils;
import com.common.lib.view.banner.ViewHolder;

public class BannerViewHolder implements ViewHolder<BannerBean> {
    private ImageView ivBanner;
    private Context mContext;

    @Override
    public View createView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_banner, null);
        ivBanner = view.findViewById(R.id.ivBanner);
        return view;
    }

    public BannerViewHolder() {

    }

    @Override
    public void onBind(Context context, int position, BannerBean bean) {
        BaseUtils.StaticParams.loadImage(mContext, 0, bean.getFileStr(), ivBanner);
    }
}
