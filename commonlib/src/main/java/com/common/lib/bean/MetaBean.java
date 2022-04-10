package com.common.lib.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MetaBean implements Serializable {

    @SerializedName("NODE")
    private Node node;
    @SerializedName("PLEDGE")
    private Pledge pledge;

    @SerializedName("POOL")
    private Pool pool;


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Pledge getPledge() {
        return pledge;
    }

    public void setPledge(Pledge pledge) {
        this.pledge = pledge;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public static class Node implements Serializable{
        private String minerAddress;
        private String POS_RevenuesAddress;
        private String POSR_RevenuesAddress;

        public String getMinerAddress() {
            return minerAddress;
        }

        public void setMinerAddress(String minerAddress) {
            this.minerAddress = minerAddress;
        }

        public String getPOS_RevenuesAddress() {
            return POS_RevenuesAddress;
        }

        public void setPOS_RevenuesAddress(String POS_RevenuesAddress) {
            this.POS_RevenuesAddress = POS_RevenuesAddress;
        }

        public String getPOSR_RevenuesAddress() {
            return POSR_RevenuesAddress;
        }

        public void setPOSR_RevenuesAddress(String POSR_RevenuesAddress) {
            this.POSR_RevenuesAddress = POSR_RevenuesAddress;
        }
    }

    public static class Pledge implements Serializable{
        private String fee;
        private String min;
        private String freezeDays;

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getFreezeDays() {
            return freezeDays;
        }

        public void setFreezeDays(String freezeDays) {
            this.freezeDays = freezeDays;
        }
    }

    public static class Pool implements Serializable {
        private String staticRate;
        private String totalProfit;

        public String getStaticRate() {
            return staticRate;
        }

        public void setStaticRate(String staticRate) {
            this.staticRate = staticRate;
        }

        public String getTotalProfit() {
            return totalProfit;
        }

        public void setTotalProfit(String totalProfit) {
            this.totalProfit = totalProfit;
        }
    }
}
