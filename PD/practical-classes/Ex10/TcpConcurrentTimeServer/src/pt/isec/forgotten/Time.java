package pt.isec.forgotten;

import java.io.Serializable;

public class Time implements Serializable {
    public static final long serialVersionUID = 11;

    private int hour;
    private int minutes;
    private int seconds;

    public Time(int hour, int minute, int second)
    {
        this.hour = hour;
        this.minutes = minute;
        this.seconds = second;
    }

    public int getHour()
    {
        return hour;
    }

    public void setHour(int hour)
    {
        this.hour = hour;
    }

    public int getMinute()
    {
        return minutes;
    }

    public void setMinute(int minute)
    {
        this.minutes = minute;
    }

    public int getSecond()
    {
        return seconds;
    }

    public void setSecond(int second) {
        this.seconds = second;
    }

    @Override
    public String toString()
    {
        /*StringWriter s = new StringWriter();
        PrintWriter p = new PrintWriter(s);
        p.format("%02d:%02d:%02d", hour, minute, second);
        return s.toString();*/

        return String.format("%02d:%02d:%02d", hour, minutes, seconds);

        //return hour + ":" + minute + ":" + second;
    }
}
