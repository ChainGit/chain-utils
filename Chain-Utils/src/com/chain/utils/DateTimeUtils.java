package com.chain.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chain.exception.ChainUtilsRuntimeException;

/**
 * Java8的时间日期工具类
 * 
 * @author Chain
 * @version 1.0
 *
 */
public class DateTimeUtils {

	private static Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);

	/**
	 * 获得指定pattern的DateTimeFormatter
	 * 
	 * @param pattern
	 *            匹配格式，如yyyy-MM-dd HH:mm:ss
	 * @return DateTimeFormatter
	 */
	public static DateTimeFormatter getDateTimeFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern);
	}

	/**
	 * 获得两个日期之间的天数差
	 * 
	 * 比如： fDate: 2017/07/02 oDate: 2017/07/26 返回为+24
	 *
	 * @param fDate
	 *            时间1
	 * @param oDate
	 *            时间2
	 * @return 时间1比时间2早多少天
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		LocalDate flocalDate = fDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate olocalDate = oDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Period period = Period.between(flocalDate, olocalDate);
		int days = period.getDays();
		return days;
	}

	public static long convertDateToLong(Date date) {
		if (date == null)
			throw new ChainUtilsRuntimeException("data is null");
		return date.getTime();
	}

	public static Date convertLongToDate(Long lo) {
		if (lo == null)
			throw new ChainUtilsRuntimeException("long is null");
		return new Date(lo);
	}

	/**
	 * 使用java8的新时间API，也可以使用Joda-Time
	 * 
	 * @param date
	 *            日期
	 * @param formatter
	 *            DateTimeFormatter
	 * @return 格式化的时间字符串
	 */
	public static String toFormatDateString(Date date, DateTimeFormatter formatter) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
		return formatter.format(localDateTime);
	}

	// 使用java8的新时间API，也可以使用Joda-Time
	public static Date parseDateFormString(String string, DateTimeFormatter formatter) {
		LocalDateTime dateTime = LocalDateTime.parse(string, formatter);
		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = dateTime.atZone(zoneId).toInstant();
		return Date.from(instant);
	}
}