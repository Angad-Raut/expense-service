package com.projectx.expense_service.utils;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

@Component
public class ExpenseUtils {
    public static final String EXPENSE_NOT_EXISTS="Expense details not present in the system!!";
    public static final String ERROR_MSG="Expense details should not be insert or update due to some technical issue!!";
    public static final String EXPENSE_EXISTS="Expense details already added for today!!";
    public static final String EXPENSE_ITEM_EXISTS="Expense item already added please try another!!";
    public static final String EXTERNAL_EXPENSE_NOT_EXISTS="External expense not exists!!";
    public static final String DASH="-";
    public static final String OPEN="Open";
    public static final String CLOSE="Closed";
    public static final String ISO_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String VIEW_DATE_FORMAT="dd MMMM yyyy";
    public static final String toExpenseDate(Date expenseDate) {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        return format.format(expenseDate);
    }
    public static Date atStartOfDay() {
        Date date = new Date();
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay() {
        Date date = new Date();
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }
    public static Date firstDayOfMonth() {
        Date currentDay = new Date();
        LocalDateTime firstDayOfMonth = dateToLocalDateTime(currentDay).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return localDateTimeToDate(firstDayOfMonth);
    }
    public static Date firstDayOfYear() {
        Date currentDay = new Date();
        LocalDateTime firstDayOfYear = dateToLocalDateTime(currentDay).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        return localDateTimeToDate(firstDayOfYear);
    }
    public static Date lastDayOfMonth() {
        Date currentDay = new Date();
        LocalDateTime lastDayOfMonth = dateToLocalDateTime(currentDay).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return localDateTimeToDate(lastDayOfMonth);
    }
    public static Date lastDayOfYear() {
        Date currentDay = new Date();
        LocalDateTime lastDayOfYear = dateToLocalDateTime(currentDay).with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
        return localDateTimeToDate(lastDayOfYear);
    }
    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date getISOStartDate(String startDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(startDate);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate).with(LocalTime.MIN));
    }
    public static Date getISODate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(date);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate));
    }
    public static Date getISOEndDate(String endDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(VIEW_DATE_FORMAT);
        Date beforeDate = format.parse(endDate);
        SimpleDateFormat ISOFormat = new SimpleDateFormat(ISO_DATE_FORMAT);
        String convertedDate = ISOFormat.format(beforeDate);
        Date finalDate = ISOFormat.parse(convertedDate);
        return localDateTimeToDate(dateToLocalDateTime(finalDate).with(LocalTime.MAX));
    }
    public static String toINRFormat(Double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        format.setCurrency(java.util.Currency.getInstance("INR"));
        String formattedAmount = format.format(amount);
        return formattedAmount;
    }
}
