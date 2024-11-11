package com.data.pipeline.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeeRequestBO {
    private String userCode;
    private int payProduct;
    private int payMode;
    private int transType;
    private int signProduct;
    private String bankType;
    private double actualAmount;

    public static FeeRequestBO parseFromLog(String log) {
        FeeRequestBO feeRequestBO = new FeeRequestBO();

        // 使用正则表达式来解析日志中的各个字段
        Pattern pattern = Pattern.compile("userCode='([^']*)',.*payProduct=(\\d+),.*payMode=(\\d+),.*transType=(\\d+),.*signProduct=(\\d+),.*bankType='([^']*)',.*actualAmount=(\\d+\\.?\\d*)");
        Matcher matcher = pattern.matcher(log);

        if (matcher.find()) {
            feeRequestBO.userCode = matcher.group(1);
            feeRequestBO.payProduct = Integer.parseInt(matcher.group(2));
            feeRequestBO.payMode = Integer.parseInt(matcher.group(3));
            feeRequestBO.transType = Integer.parseInt(matcher.group(4));
            feeRequestBO.signProduct = Integer.parseInt(matcher.group(5));
            feeRequestBO.bankType = matcher.group(6);
            feeRequestBO.actualAmount = Double.parseDouble(matcher.group(7));
        }

        return feeRequestBO;
    }

    @Override
    public String toString() {
        return "FeeRequestBO{" +
            "userCode='" + userCode + '\'' +
            ", payProduct=" + payProduct +
            ", payMode=" + payMode +
            ", transType=" + transType +
            ", signProduct=" + signProduct +
            ", bankType='" + bankType + '\'' +
            ", actualAmount=" + actualAmount +
            '}';
    }

    public static void main(String[] args) {
        String log = "2024-11-06 09:29:25.404|ERROR|cbs-fee-calculate|10.42.63.35|c.y.c.b.f.s.s.i.TradeFeeCalculationServiceImpl|DubboServerHandler-10.153.34.106:28081-thread-97|ab622d64ab95438095f197c793ad2809.3195.17308565653743069||||计费配置匹配失败(有模板未命中)，计费请求参数：FeeRequestBO[tranSysNo='1234567890', sysNoFlag=1, billingFlag=1, paymentState=2, actualAmount=100, feeObjectFlag='null', originatorFlag='1', payeeFlag='1', payerFlag='null', originatorFee=null, originatorPreciseFee='null', originatorFeeMode=null, payeeFee=null, payeePreciseFee='null', payeeFeeMode=null, payerFee=null, payerPreciseFee='null', payerFeeMode=null, currency='CNY', feeTime=null, payProduct=13, payMode=13, transType=10, signProduct=12, bankType='1902000', bankAccountType='null', areaFlag='null', userCardOrg='null', areaCode='null', feeCalculateTime=2024-11-06T09:29:25.402, existPayerFee=null, orderAmount=100, originatorFeeConfigAuthFlag=1, originatorCustId='2023091330427085', outOrgNo='null', userCode='QRY241106499568', busiCode='00510030', oldPayMode='null']";

        FeeRequestBO feeRequestBO = FeeRequestBO.parseFromLog(log);
        System.out.println(feeRequestBO);
    }
}
