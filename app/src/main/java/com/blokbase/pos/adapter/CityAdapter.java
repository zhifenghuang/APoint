package com.blokbase.pos.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.common.lib.bean.DistrictBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CityAdapter extends BaseQuickAdapter<DistrictBean, BaseViewHolder> {

    private Context mContext;

    public CityAdapter(Context context) {
        super(R.layout.item_city);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, DistrictBean bean) {
        helper.setText(R.id.tvText, bean.getText())
                .setTextColor(R.id.tvText, ContextCompat.getColor(mContext, bean.isSelect() ? R.color.text_color_5 : R.color.text_color_2));
    }

    public void showSelect(int position) {
        List<DistrictBean> list = getData();
        int size = list.size();
        for (int i = 0; i < size; ++i) {
            list.get(i).setSelect(position == i);
        }
        notifyDataSetChanged();
    }
}
