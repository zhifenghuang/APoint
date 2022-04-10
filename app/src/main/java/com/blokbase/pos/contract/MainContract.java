package com.blokbase.pos.contract;

import com.common.lib.bean.AssetsBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.bean.QuotationsBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface MainContract {
    public interface View extends IView {
        public void getAssetsListSuccess(ArrayList<AssetsBean> list);

        public void checkVersionSuccess(VersionBean bean);

        public void getHangQingSuccess(QuotationsBean bean);
    }

    public interface Presenter extends IPresenter {
        public void assetsList();

        public void checkVersion();

        public void getHangQing();
    }
}
