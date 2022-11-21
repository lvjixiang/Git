package Jxiang;

import java.util.Calendar;
import java.util.Date;

/**
 * 投资项目
 */
public class Investment {
    private String id; // 项目号 12位数字
    private String projectName; // 项目名
    private Double estimatedRevenue; // 预计收入
    private Double rate; // 投资回报率
    private Date date; // 购买资金的日期
    private Double time; // 周期时长
    private Date dateLast; // 年盈利日期

    public Date getDateLast() {
        return dateLast;
    }

    public void setDateLast(Date dateLast) {
        this.dateLast = dateLast;
    }

    public Investment() {
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Investment(String id, String projectName, Double rate, Double time) {
        this.id = id;
        this.projectName = projectName;
        this.rate = rate;
        this.time = time;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getEstimatedRevenue() {
        return estimatedRevenue;
    }

    public void setEstimatedRevenue(Double estimatedRevenue) {

        this.estimatedRevenue = (estimatedRevenue * rate);

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        Date date1 = new Date();
        date1.setTime(date.getTime());
        long time = date1.getTime();
        for (int i = 0; i < 365 * (this.time); i++) {
            time += (1000*60*60*24);
        }

        date1.setTime(time);
        this.dateLast = date1;
    }
    public void change(){

    }
}