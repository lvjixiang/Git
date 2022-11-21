package Jxiang;

import java.util.Calendar;
import java.util.Date;

/**
 * 还款记录
 */
public class log {
    Date date; // 还款日期
    Integer year; // 年
    Integer month; // 月
    Integer day; // 日
    Double benjin; // 本金
    Double lixi; // 利息
    Double tax; // 契据保存费用
    String tag; // 是否已还款
    Double cha; // 是否由MSG补足
    Double allMoney;
    Double remainingRepaymentAmount; //剩余还款金额
    Double saving; //存款
    Calendar cal = Calendar.getInstance();
    public log() {
    }



    public log(Date date, Double benjin, Double lixi, Double tax,Double cha,Double remainingRepaymentAmount,Double saving) {
        Date date1 = new Date();
        date1.setTime(date.getTime());
        this.date = date1;
        this.benjin = benjin;
        this.lixi = lixi;
        this.tax = tax;
        this.allMoney = benjin + lixi + tax;
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        this.cha = cha;
        this.remainingRepaymentAmount = remainingRepaymentAmount;
        this.saving = saving;
    }

    public Double getCha() {
        return cha;
    }

    public void setCha(Double cha) {
        this.cha = cha;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getDate() {
        return year + "年" + month + "月" + day + "日";
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getBenjin() {
        return benjin;
    }

    public void setBenjin(Double benjin) {
        this.benjin = benjin;
    }

    public Double getLixi() {
        return lixi;
    }

    public void setLixi(Double lixi) {
        this.lixi = lixi;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }
}
