package com.blokbase.pos.contract;

import com.common.lib.bean.DistrictBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface AddAddressContract {
    public interface View extends IView {
        public void addAddressSuccess();

        public void saveAddressSuccess();
    }

    public interface Presenter extends IPresenter {

        public void addAddress(String name,
                               String mobile,
                               String provinceCode,
                               String provinceName,
                               String cityCode,
                               String cityName,
                               String districtCode,
                               String districtName,
                               String detail,
                               int isDefault);

        public void saveAddress(
                int id,
                String name,
                String mobile,
                String provinceCode,
                String provinceName,
                String cityCode,
                String cityName,
                String districtCode,
                String districtName,
                String detail,
                int isDefault);
    }
}
