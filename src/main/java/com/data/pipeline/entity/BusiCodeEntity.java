package com.data.pipeline.entity;

public class BusiCodeEntity {

    String busiCode;
    String tran;

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getTran() {
        return tran;
    }

    public void setTran(String tran) {
        this.tran = tran;
    }

    @Override
    public String toString() {
        return "BusiCodeEntity{" +
                "busiCode='" + busiCode + '\'' +
                ", tran='" + tran + '\'' +
                '}';
    }
}
