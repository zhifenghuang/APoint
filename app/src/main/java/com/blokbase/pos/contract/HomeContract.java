package com.blokbase.pos.contract;

import com.common.lib.bean.CheckInBean;
import com.common.lib.bean.GoodsBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface HomeContract {
    public interface View extends IView {
        public void getGoodsListSuccess(int page, int goodsType, ArrayList<GoodsBean> list);

        public void getGoodsListFailed();
    }

    public interface Presenter extends IPresenter {
        public void goodsList(int page, int goodsType, boolean isMore);
    }
}
