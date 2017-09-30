package kalei.com.timerapp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.Interval;
import org.joda.time.Period;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TimeDifference {

    public TimeDifference() {
    }

    public static final List<Long> times = Arrays.asList(
            DAYS.toMillis(365),
            DAYS.toMillis(30),
            DAYS.toMillis(7),
            DAYS.toMillis(1),
            HOURS.toMillis(1),
            MINUTES.toMillis(1),
            SECONDS.toMillis(1)
    );

    public static final List<String> timesString = Arrays.asList(
            "yr", "mo", "wk", "day", "hr", "min", "sec"
    );

    /**
     * Get relative time ago for date
     * <p>
     * NOTE:
     * if (duration > WEEK_IN_MILLIS) getRelativeTimeSpanString prints the date.
     * <p>
     * ALT:
     * return getRelativeTimeSpanString(date, now, SECOND_IN_MILLIS, FORMAT_ABBREV_RELATIVE);
     *
     * @param date String.valueOf(TimeUtils.getRelativeTime(1000L * Date/Time in Millis)
     *
     * @return relative time
     */
    public static CharSequence getRelativeTime(final long date) {
        return toDuration(Math.abs(System.currentTimeMillis() - date));
    }

    public static String getFormattedStringDate(Date startDate, Date endDate) {

        String returnString = "";
        try {
            Interval interval =
                    new Interval(startDate.getTime(), endDate.getTime());
            Period period = interval.toPeriod();

            if (period.getYears() > 0) {
                returnString += period.getYears() + " years ";
            }
            if (period.getMonths() > 0) {
                returnString += period.getMonths() + " months ";
            }
            if (period.getDays() > 7) {
                int weeks = period.getDays() / 7;
                returnString += weeks + " weeks ";
            }
            if (period.getDays() < 7) {
                returnString += period.getDays() + " days";
            }
        } catch (IllegalArgumentException e) {
            returnString = "you cannot set this in the future. ";
        }
        return returnString;
    }

    private static String toDuration(long duration) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                sb.append(temp)
                        .append(" ")
                        .append(timesString.get(i))
                        .append(temp > 1 ? "s" : "")
                        .append(" ago");
                break;
            }
        }
        return sb.toString().isEmpty() ? "now" : sb.toString();
    }
}
