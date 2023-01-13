package com.blokbase.pos.contract;

import com.common.lib.bean.ReceiveAddressBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface AddressListContract {
    public interface View extends IView {
        public void getAddressListSuccess(ArrayList<ReceiveAddressBean> list);

        public void deleteAddressSuccess(ReceiveAddressBean bean);

        public void setAddressDefaultSuccess(ReceiveAddressBean bean);

    }

    public interface Presenter extends IPresenter {
        public void addressList();

        public void deleteAddress(ReceiveAddressBean bean);

        public void setAddressDefault(ReceiveAddressBean bean);
    }
}
