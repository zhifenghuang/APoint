package com.blokbase.pos.contract;

import com.common.lib.bean.DistrictBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface SelectCityContract {
    public interface View extends IView {
        public void getDistrictListSuccess(int index, String pCode, ArrayList<DistrictBean> list);
    }

    public interface Presenter extends IPresenter {
        public void districtList(int index, String pCode);
    }
}
