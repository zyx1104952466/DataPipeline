package com.data.pipeline.entity;

public class FeeEntity {

    String amount;
    String payerCustId;
    String bankAccountType;
    String payeeCustId;
    String bankType;
    String payMode;
    String busiCode;
    String outId;
    String srcCustId;
    String tranType;
    String areacode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayerCustId() {
        return payerCustId;
    }

    public void setPayerCustId(String payerCustId) {
        this.payerCustId = payerCustId;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getPayeeCustId() {
        return payeeCustId;
    }

    public void setPayeeCustId(String payeeCustId) {
        this.payeeCustId = payeeCustId;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public String getSrcCustId() {
        return srcCustId;
    }

    public void setSrcCustId(String srcCustId) {
        this.srcCustId = srcCustId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    @Override
    public String toString() {
        return "FeeEntity{" +
                "amount='" + amount + '\'' +
                ", payerCustId='" + payerCustId + '\'' +
                ", bankAccountType='" + bankAccountType + '\'' +
                ", payeeCustId='" + payeeCustId + '\'' +
                ", bankType='" + bankType + '\'' +
                ", payMode='" + payMode + '\'' +
                ", busiCode='" + busiCode + '\'' +
                ", outId='" + outId + '\'' +
                ", srcCustId='" + srcCustId + '\'' +
                ", tranType='" + tranType + '\'' +
                ", areacode='" + areacode + '\'' +
                '}';
    }
}
