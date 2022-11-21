package Jxiang;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    long time;
    Calendar cal = Calendar.getInstance();
    private Date date;

    public MyTimerTask(Date taskName) {
        this.time = taskName.getTime();
        this.date = taskName;
    }

    public Date getTaskName() {
        return date;
    }

    public void setTaskName(Date taskName) {
        this.date = taskName;
    }

    @Override
    public void run() {

        time += (1000);
        date.setTime(time);
    }

}