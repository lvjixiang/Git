package Jxiang;

import java.util.ArrayList;
import java.util.List;

public class MSG {
    private double AllMoney;
    private Double Annualoperatingexpenses = 10000.0; // 年运营费用
    private Double zijin; // 资金
    private List<logMsg> logMsgList = new ArrayList<>(); // msg资金记录

    public List<logMsg> getLogMsgList() {
        return logMsgList;
    }

    public void setLogMsgList(List<logMsg> logMsgList) {
        this.logMsgList = logMsgList;
    }

    private List<Investment> investmentList = new ArrayList<>(); // 投资列表

    public List<Investment> getInvestmentList() {
        return investmentList;
    }

    public void setInvestmentList(List<Investment> investmentList) {
        this.investmentList = investmentList;
    }

    public double getAllMoney() {
        return AllMoney;
    }

    public Double getAnnualoperatingexpenses() {
        return Annualoperatingexpenses;
    }

    public void setAnnualoperatingexpenses(Double annualoperatingexpenses) {
        Annualoperatingexpenses = annualoperatingexpenses;
    }

    public void setAllMoney(double allMoney) {
        AllMoney = allMoney;
    }
}
