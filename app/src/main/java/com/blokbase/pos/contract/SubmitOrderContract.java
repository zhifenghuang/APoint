package com.blokbase.pos.contract;

import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface SubmitOrderContract {
    public interface View extends IView {
        public void submitOrderSuccess();

        public void getDefaultAddressSuccess(ReceiveAddressBean bean);

        public void getAssetsListSuccess(ArrayList<AssetsBean> list);
    }

    public interface Presenter extends IPresenter {
        public void submitOrder(int addressId, int orderType, String uaaPayAmount, String payPassword, List<HashMap<String, Object>> goods);

        public void getDefaultAddress();

        public void assetsList();
    }
}
