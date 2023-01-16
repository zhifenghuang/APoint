package com.blokbase.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.LoginActivity;
import com.blokbase.pos.activity.MainActivity;
import com.common.lib.activity.BaseActivity;
import com.common.lib.manager.DataManager;
import com.common.lib.view.banner.ViewHolder;

public class GuideBannerViewHolder implements ViewHolder<Integer> {
    private Context mContext;
    private ImageView ivPic;

    @Override
    public View createView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide, null);
        ivPic = view.findViewById(R.id.ivPic);
        return view;
    }


    @Override
    public void onBind(Context context, int position, Integer bean) {
        ivPic.setImageResource(bean);
        if (position == 2) {
            ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) mContext).openActivity(DataManager.Companion.getInstance().getMyInfo() == null ?
                            LoginActivity.class : MainActivity.class);
                    ((BaseActivity) mContext).finish();
                }
            });
        }
    }
}
