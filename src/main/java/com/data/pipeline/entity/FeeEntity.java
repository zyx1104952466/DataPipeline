package com.data.pipeline.entity;

/**
 * @author zhangyux
 * @since 2024/7/16 上午11:59
 */
public class FeeEntity {

    String mercId;
    String busiType;
    String cardType;
    String feeMode;
    String carryMode;
    String feeCalType;
    String minFeeAmt;
    String maxFeeAmt;
    String feeRate;
    String fixFeeAmt;

    public String getMercId() {
        return mercId;
    }

    public void setMercId(String mercId) {
        this.mercId = mercId;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getFeeMode() {
        return feeMode;
    }

    public void setFeeMode(String feeMode) {
        this.feeMode = feeMode;
    }

    public String getCarryMode() {
        return carryMode;
    }

    public void setCarryMode(String carryMode) {
        this.carryMode = carryMode;
    }

    public String getFeeCalType() {
        return feeCalType;
    }

    public void setFeeCalType(String feeCalType) {
        this.feeCalType = feeCalType;
    }

    public String getMinFeeAmt() {
        return minFeeAmt;
    }

    public void setMinFeeAmt(String minFeeAmt) {
        this.minFeeAmt = minFeeAmt;
    }

    public String getMaxFeeAmt() {
        return maxFeeAmt;
    }

    public void setMaxFeeAmt(String maxFeeAmt) {
        this.maxFeeAmt = maxFeeAmt;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getFixFeeAmt() {
        return fixFeeAmt;
    }

    public void setFixFeeAmt(String fixFeeAmt) {
        this.fixFeeAmt = fixFeeAmt;
    }

    @Override
    public String toString() {
        return "FeeEntity{" +
                "mercId='" + mercId + '\'' +
                ", busiType='" + busiType + '\'' +
                ", cardType='" + cardType + '\'' +
                ", feeMode='" + feeMode + '\'' +
                ", carryMode='" + carryMode + '\'' +
                ", feeCalType='" + feeCalType + '\'' +
                ", minFeeAmt='" + minFeeAmt + '\'' +
                ", maxFeeAmt='" + maxFeeAmt + '\'' +
                ", feeRate='" + feeRate + '\'' +
                ", fixFeeAmt='" + fixFeeAmt + '\'' +
                '}';
    }

}
