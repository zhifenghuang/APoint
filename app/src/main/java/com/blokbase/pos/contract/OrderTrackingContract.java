package com.blokbase.pos.contract;

import com.common.lib.bean.OrderBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface OrderTrackingContract {
    public interface View extends IView {
        public void getOrderLogisticsSuccess(OrderBean orderBean);

    }

    public interface Presenter extends IPresenter {
        public void orderLogistics(int id, String expressCode, String expressNo);
    }
}
