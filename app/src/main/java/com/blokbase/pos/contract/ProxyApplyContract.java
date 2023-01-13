package com.blokbase.pos.contract;

import com.common.lib.bean.AgentBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface ProxyApplyContract {
    public interface View extends IView {
        public void getAgentGoodsSuccess(ArrayList<AgentBean> list);


        public void submitAgentSuccess();

    }

    public interface Presenter extends IPresenter {
        public void submitAgent(String id, String code, String payPassword, String districtText);

        public void agentGoods();
    }
}
