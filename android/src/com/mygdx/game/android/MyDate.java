package com.mygdx.game.android;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrateur on 14/04/14.
 */
public class MyDate {
    public int year, month, day;

    public MyDate(String str) {
        year = Integer.valueOf(str.substring(0, 4));
        month = Integer.valueOf(str.substring(4, 6));
        day = Integer.valueOf(str.substring(6, 8));
    }

    public MyDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    public void setDate(String str) {
        year = Integer.valueOf(str.substring(0, 4));
        month = Integer.valueOf(str.substring(4, 6));
        day = Integer.valueOf(str.substring(6, 8));
    }

    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
    }

    public boolean isSame(MyDate other) {
        return (year == other.year && month == other.month && day == other.day);
    }

    public boolean isSuperiorThan(MyDate other) {
        if (year > other.year)
            return true;
        else if (year == other.year) {
            if (month > other.month)
                return true;
            else if (month == other.month) {
                if (day > other.day)
                    return true;
            }
        }
        return false;
    }

    public boolean isLowerThan(MyDate other) {
        if (year < other.year)
            return true;
        else if (year == other.year) {
            if (month < other.month)
                return true;
            else if (month == other.month) {
                if (day < other.day)
                    return true;
            }
        }
        return false;
    }
}
