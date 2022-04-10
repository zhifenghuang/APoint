package com.common.lib.bean;

import java.util.ArrayList;

public class TickerBean {
    private String status;
    private long timestamp;
    private ArrayList<QuotationsBean> ticker;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<QuotationsBean> getTicker() {
        return ticker;
    }

    public void setTicker(ArrayList<QuotationsBean> ticker) {
        this.ticker = ticker;
    }
}
