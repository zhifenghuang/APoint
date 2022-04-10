package com.blokbase.pos.contract;

import com.common.lib.bean.PoolNodeRankBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface PoolNodeRankContract {

    public interface View extends IView {
        public void getPoolNodeRankSuccess(int page, ArrayList<PoolNodeRankBean> list);

        public void getPoolNodeRankFailed();
    }

    public interface Presenter extends IPresenter {
        public void poolNodeRank(int page, int gradeId);
    }
}
