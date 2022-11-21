package Jxiang;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Accounts {
    // 日期格式化
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    DecimalFormat df = new DecimalFormat("0.00");

    // 主要参数属性（构造器）
    Double loanAmount; // 贷款金额
    Integer lengthOfLoan; // 贷款时长（年）
    Double yearInterestRate; // 年利率

    // 其他属性
    Double monthlyInterestRate; // 月利率
    Double lengthofloanMonth; // 贷款时长（月）
    Double mustPay; // 固定还款

    Date date; // 抵押时间

    double moneyStatic; // 记录应还金额
    double remainingRepaymentAmount; // 剩余还款金额

    private Taxation taxation; // 税费


    List<log> logs = new ArrayList<>();
    public double getMoneyStatic() {
        return moneyStatic;
    }

    public List<log> getLogs() {
        return logs;
    }

    public void setLogs(List<log> logs) {
        this.logs = logs;
    }

    public Accounts() {
    }

    public Taxation getTaxation() {
        return taxation;
    }

    public void setTaxation(Taxation taxation) {
        this.taxation = taxation;
    }

    public Accounts(Double loanAmount, Integer lengthOfLoan, Double yearInterestRate, Date date, Double housePirce) {
        this.loanAmount = loanAmount;
        this.lengthOfLoan = lengthOfLoan;
        this.yearInterestRate = yearInterestRate;
        this.monthlyInterestRate = yearInterestRate / 12 / 100; // 月利息 = 年利息 / 12;
        this.lengthofloanMonth = lengthOfLoan * 12.0; // 贷款期限（月） = 贷款期限（年） * 12;
        /**
         * 每月还款额计算公式如下：
         * [贷款本金×月利率×（1+月利率）^还款月数]÷[（1+月利率）^还款月数－1]
         */
        this.mustPay = (loanAmount * monthlyInterestRate * Math.pow((1 + monthlyInterestRate),lengthofloanMonth)) / (Math.pow((1 + monthlyInterestRate),lengthofloanMonth) - 1);
        remainingRepaymentAmount = this.loanAmount; // 剩余还款金额
        this.date = date; // 抵押时间
        this.taxation = new Taxation(housePirce);

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getMustPay() {
        return mustPay;
    }

    /**
     * 还款操作
     */

    public void reimbursement(MSG msg, Date date, User user){
        // 统一按月
        // 每月固定还款


        // 判断是否还有贷款：
        if (this.remainingRepaymentAmount <= 0.5) {
            this.remainingRepaymentAmount = 0.0;
            return;
        }
        // 夫妻收入不足情况还款
        Double cha = user.getSpouse().getWage() * 0.28 - user.getSpouse().getAccount().getMustPay();

        if (cha <= 0){
            msg.setAllMoney(msg.getAllMoney() + cha);
            System.out.println("夫妻收入不足，MSG垫付：" + df.format(Math.abs(cha)));
        }


        //

        // 应付利息（不断改变）
        Double lixi = monthlyInterestRate * remainingRepaymentAmount;

        // 偿还资金是购买房价的1/360
        Double benjin = this.mustPay - lixi;

        // 财产税和保险费
        // 假设每年1000;
        //Double tax = 1000.0 / 12;
        Double tax = taxation.getPropertyTax()/12 + taxation.getInsurance()/12;

        // Double allMoney = benjin + lixi + tax;
        msg.setAllMoney(msg.getAllMoney() + this.getMustPay());
        remainingRepaymentAmount = remainingRepaymentAmount - benjin;
        logs.add(new log(date,benjin,lixi,tax,cha,remainingRepaymentAmount,user.getSpouse().getSavings()));


        System.out.println("----------还款账单----------\n"+"ID：" + user.getAccountNumber() + "\n还款日期：" + dateFormat.format(date) + "\n总还款：" + df.format(this.mustPay) + "\n详细还款信息\n还款本金：" + df.format(benjin) +
                "\n应付利息：" + df.format(lixi) +
                "\n契据保存费用：" + df.format(tax) +
                "\n"+"当前剩余还款金额：" + df.format(this.remainingRepaymentAmount) + "\n--------------------------");
    }
}
