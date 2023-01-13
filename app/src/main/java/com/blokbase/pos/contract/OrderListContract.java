package com.blokbase.pos.contract;

import com.common.lib.bean.OrderBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface OrderListContract {
    public interface View extends IView {
        public void getOrderListSuccess(int pageNo, ArrayList<OrderBean> list);

        public void getOrderListFailed();

        public void orderCompleteSuccess(OrderBean bean);

    }

    public interface Presenter extends IPresenter {
        public void orderList(int pageNo, int deliveryStatus);

        public void orderComplete(OrderBean bean);
    }
}
