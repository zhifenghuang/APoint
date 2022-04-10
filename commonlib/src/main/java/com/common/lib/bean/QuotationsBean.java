package com.common.lib.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class QuotationsBean implements Serializable {
    private String symbol;
    private String high;
    private String vol;
    private String last;
    private String low;
    private String buy;
    private String sell;
    private double change;


    public String getSymbol() {
        return symbol;
    }

    public String getSymbol2() {
        if (!TextUtils.isEmpty(symbol) && symbol.toUpperCase().endsWith("_USDT")) {
            symbol = symbol.substring(0, symbol.length() - 5).toUpperCase();
        }
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getLast() {
        if (!TextUtils.isEmpty(last)) {
            try {
                last = String.format("%.2f", Float.parseFloat(last));
            }catch (Exception e){

            }
        }
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}
