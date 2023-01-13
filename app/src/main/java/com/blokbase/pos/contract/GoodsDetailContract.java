package com.blokbase.pos.contract;

import com.common.lib.bean.GoodsBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface GoodsDetailContract {
    public interface View extends IView {
        public void getGoodsDetailSuccess(GoodsBean bean);
    }

    public interface Presenter extends IPresenter {
        public void goodsDetail(int id);
    }
}
