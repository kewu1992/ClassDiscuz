package cmu.banana.classdiscuz.util;

/**
 * Parse the time string from database to a new format
 */
public class ParseTime {
    public long getStartTime(String time) {
        String[] tokens = time.split("[ -]");

        //split the start time using ":"
        String[] mTime = tokens[1].split("[:]");
        return Integer.parseInt(mTime[0]) * 60 + Integer.parseInt(mTime[1]);
    }

    public long getEndTime(String time) {
        String[] tokens = time.split("[ -]");

        //split the end time using ":"
        String[] mTime2 = tokens[2].split("[:]");
        return Integer.parseInt(mTime2[0])* 60 + Integer.parseInt(mTime2[1]);
    }

    public boolean checkWeek(String time, int weekMark) {
        String week;
        String week_str;

        String[] tokens = time.split("[ -]");

        week = tokens[0];

        switch (weekMark) {
            case 2: week_str = "M"; break;
            case 3: week_str = "T"; break;
            case 4: week_str = "W"; break;
            case 5: week_str = "R"; break;
            case 6: week_str = "F"; break;
            default: week_str = "x"; break;

        }
        if (week.contains(week_str) == true) {
            return true;
        }
        else
            return false;
    }


}
