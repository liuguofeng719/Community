package com.community;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuguofeng719 on 2016/8/20.
 */
public class DateTest {
    @Test
    public void testDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long time1 = sdf.parse("2012-01-12").getTime();
        long time2 = sdf.parse("2013-02-11").getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(time1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(time2);
        System.out.println(calendar1.before(calendar2));
        String startCal = "2016-8-10";
        String endCal = "2016-10-7";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf1.parse(startCal);
            Date endDate = sdf1.parse(endCal);
            Calendar startCalendar = Calendar.getInstance();
            Calendar endCalendar = Calendar.getInstance();

            startCalendar.setTime(startDate);
            endCalendar.setTime(endDate);

            if (endCalendar.compareTo(startCalendar) < 0) {
                System.out.println("后一时次的日期小于前一时次的日期，请重新输入。");
                return;
            }

            int day = endCalendar.get(Calendar.DAY_OF_MONTH)
                    - startCalendar.get(Calendar.DAY_OF_MONTH);
            int month = endCalendar.get(Calendar.MONTH)
                    - startCalendar.get(Calendar.MONTH);
            int year = endCalendar.get(Calendar.YEAR)
                    - startCalendar.get(Calendar.YEAR);

            if (day < 0) {
                month--;
            }

            if (month < 0) {
                month += 12;
                year--;
            }
            System.out.println("两者相差年月为：" + year + "年" + month + "个月");
            System.out.println(getMonthNum(startDate, endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static int getMonthNum(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12 + (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));

    }
}
