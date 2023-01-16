package com.blokbase.pos.contract;

import com.common.lib.bean.PosterBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface InviteContract {

    public interface View extends IView {
        public void getPosterSuccess(PosterBean poster);
    }

    public interface Presenter extends IPresenter {
        public void poster();
    }
}
