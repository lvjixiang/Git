package Jxiang;

import java.util.Date;

public class logMsg {
    private Double available; // 当月可用资金
    private Double incomeInvest; // 当月投资收入
    private Double mothoperatingexpenses; // 当月运营费用
    private Double userMoney; // 抵押贷款付款总额
    private Double zengkuan; // 增宽总额

    private Date date; // 时间



    public logMsg(Double incomeInvest, Double mothoperatingexpenses, Double userMoney, Double zengkuan, Date date) {
        this.incomeInvest = incomeInvest;
        this.mothoperatingexpenses = mothoperatingexpenses;
        this.userMoney = userMoney;
        this.zengkuan = zengkuan;
        this.date = date;
        this.available = incomeInvest - mothoperatingexpenses + userMoney - zengkuan;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }

    public Double getIncomeInvest() {
        return incomeInvest;
    }

    public void setIncomeInvest(Double incomeInvest) {
        this.incomeInvest = incomeInvest;
    }

    public Double getMothoperatingexpenses() {
        return mothoperatingexpenses;
    }

    public void setMothoperatingexpenses(Double mothoperatingexpenses) {
        this.mothoperatingexpenses = mothoperatingexpenses;
    }

    public Double getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Double userMoney) {
        this.userMoney = userMoney;
    }

    public Double getZengkuan() {
        return zengkuan;
    }

    public void setZengkuan(Double zengkuan) {
        this.zengkuan = zengkuan;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
