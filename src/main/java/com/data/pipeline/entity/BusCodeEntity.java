package com.data.pipeline.entity;

public class BusCodeEntity {

    String busCode;
    String tranId;

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    @Override
    public String toString() {
        return "BusCodeEntity{" +
                "busCode='" + busCode + '\'' +
                ", tranId='" + tranId + '\'' +
                '}';
    }
}
