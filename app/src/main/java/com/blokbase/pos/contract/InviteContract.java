package com.blokbase.pos.contract;

import com.common.lib.bean.InviteBean;
import com.common.lib.bean.PosterBean;
import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface InviteContract {

    public interface View extends IView {
        public void getPosterSuccess(PosterBean poster);

        public void getInviteDetailSuccess(int pageIndex, int totalCount, ArrayList<InviteBean> list);

        public void getInviteDetailFailed();
    }

    public interface Presenter extends IPresenter {
        public void poster();

        public void inviteDetail(int pageIndex);
    }
}
