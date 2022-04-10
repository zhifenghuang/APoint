package com.blokbase.pos.contract;

import com.common.lib.bean.BannerBean;
import com.common.lib.bean.HomeDataBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface HomeContract {
    public interface View extends IView {
        public void getBannerListSuccess(ArrayList<BannerBean> list);

        public void getNoticeListSuccess(ArrayList<NoticeBean> list);

        public void getHomePosDataSuccess(HomeDataBean bean);
    }

    public interface Presenter extends IPresenter {
        public void bannerList();

        public void noticeList();

        public void homeData();
    }
}
