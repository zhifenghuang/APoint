package com.blokbase.pos.contract;

import com.common.lib.bean.NoticeBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface NoticeListContract {

    public interface View extends IView {
        public void getNoticeListSuccess(int page, ArrayList<NoticeBean> list);
        public void getNoticeListFailed();
    }

    public interface Presenter extends IPresenter {
        public void noticeList(int page);
        public void readNotice(String id);
    }
}
