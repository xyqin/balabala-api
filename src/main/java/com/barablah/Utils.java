package com.barablah;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xyqin on 2017/4/7.
 */
public final class Utils {

    private static final ExecutorService THREAD_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final RandomStringGenerator ALPHABETIC_GENERATOR = new RandomStringGenerator.Builder()
            .withinRange('A', 'z')
            .filteredBy(CharacterPredicates.LETTERS)
            .build();

    private static final RandomStringGenerator NUMERIC_GENERATOR = new RandomStringGenerator.Builder()
            .withinRange('0', '9')
            .filteredBy(CharacterPredicates.DIGITS)
            .build();

    private static final RandomStringGenerator ALPHANUMERIC_GENERATOR = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
            .build();

    private static final RandomStringGenerator ASCII_GENERATOR = new RandomStringGenerator.Builder().withinRange(33, 126).build();

    /**
     * 金额转换成字符串
     *
     * @param money
     * @return
     */
    public static String amountToString(BigDecimal money) {
        return money.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    /**
     * 根据字母生成随机字符串
     *
     * @param count
     * @return
     */
    public static String randomAlphabetic(int count) {
        return ALPHABETIC_GENERATOR.generate(count);
    }

    /**
     * 根据数字生成随机字符串
     *
     * @param count
     * @return
     */
    public static String randomNumeric(int count) {
        return NUMERIC_GENERATOR.generate(count);
    }

    /**
     * 根据字母和数字生成随机字符串
     *
     * @param count
     * @return
     */
    public static String randomAlphanumeric(int count) {
        return ALPHANUMERIC_GENERATOR.generate(count);
    }

    /**
     * 根据ASCII字符生成随机字符串
     *
     * @param count
     * @return
     */
    public static String randomAscii(int count) {
        return ASCII_GENERATOR.generate(count);
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }

        final Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        final Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static Date getFirstDayOfMonth(Date date) {
        Date firstDay = DateUtils.setDays(date, 1);
        firstDay = DateUtils.setHours(firstDay, 0);
        firstDay = DateUtils.setMinutes(firstDay, 0);
        firstDay = DateUtils.setSeconds(firstDay, 0);
        firstDay = DateUtils.setMilliseconds(firstDay, 0);
        return firstDay;
    }

    public static BigDecimal nullToZero(BigDecimal decimal) {
        return (decimal == null) ? BigDecimal.ZERO : decimal;
    }

}
