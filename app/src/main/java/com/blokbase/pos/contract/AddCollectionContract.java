package com.blokbase.pos.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.HashMap;

public interface AddCollectionContract {

    public interface View extends IView {
        public void addCollectionSuccess();
    }

    public interface Presenter extends IPresenter {
        public void addCollection(String type, String remark, String link);
    }
}
