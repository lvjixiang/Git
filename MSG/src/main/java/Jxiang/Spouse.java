package Jxiang;

import java.util.Date;

public class Spouse {
    private String maleName;
    private String femaleName;
    private Double incomeMale;
    private Double incomeFemale;
    private Double Wage; // 每月收入
    private Date lastWagechange; // 月收入改变日期
    private int lengthOfMarriage; // 结婚时长
    private Accounts account; // 申请贷款信息
    private Double savings; // 夫妻储蓄
    private boolean flag; // 前一年全日工作至少48个星期
    private Double housePrice;// 房子的最初购买价格

    private Taxation taxation; // 税费

    public Spouse() {
    }

    public Double getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(Double housePrice) {
        this.housePrice = housePrice;
    }

    public Date getLastWagechange() {
        return lastWagechange;
    }

    public void setLastWagechange(Date lastWagechange) {
        this.lastWagechange = lastWagechange;
    }

    public Spouse(String maleName, String femaleName, Double incomeMale, Double incomeFemale, int lengthOfMarriage) {
        this.maleName = maleName;
        this.femaleName = femaleName;
        this.incomeMale = incomeMale;
        this.incomeFemale = incomeFemale;
        this.lengthOfMarriage = lengthOfMarriage;
        this.Wage = incomeFemale + incomeMale;
        this.savings = incomeFemale + incomeMale;
        this.lastWagechange = new Date();
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
    }

    public Double getSavings() {
        return savings;
    }

    public void setSavings(Double savings) {
        this.savings = savings;
    }

    public String getMaleName() {
        return maleName;
    }

    public void setMaleName(String maleName) {
        this.maleName = maleName;
    }

    public String getFemaleName() {
        return femaleName;
    }

    public void setFemaleName(String femaleName) {
        this.femaleName = femaleName;
    }

    public Double getIncomeMale() {
        return incomeMale;
    }

    public void setIncomeMale(Double incomeMale) {
        this.incomeMale = incomeMale;
    }

    public Double getIncomeFemale() {
        return incomeFemale;
    }

    public void setIncomeFemale(Double incomeFemale) {
        this.incomeFemale = incomeFemale;
    }

    public void setWage(Double wage) {
        Wage = wage;
    }

    public int getLengthOfMarriage() {
        return lengthOfMarriage;
    }

    public void setLengthOfMarriage(int lengthOfMarriage) {
        this.lengthOfMarriage = lengthOfMarriage;
    }

    public double getWage() {
        return Wage;
    }

    public void setWage(double wage) {
        Wage = wage;
    }

    @Override
    public String toString() {
        return "Spouse{" +
                "maleName='" + maleName + '\'' +
                ", femaleName='" + femaleName + '\'' +
                ", incomeMale=" + incomeMale +
                ", incomeFemale=" + incomeFemale +
                ", Wage=" + Wage +
                ", lengthOfMarriage=" + lengthOfMarriage +
                '}';
    }
}
