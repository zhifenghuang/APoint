package com.blokbase.pos.contract;

import com.common.lib.bean.GoodsBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface SearchContract {
    public interface View extends IView {
        public void getGoodsListSuccess(ArrayList<GoodsBean> list);
    }

    public interface Presenter extends IPresenter {
        public void searchGoods(String keywords);
    }
}
