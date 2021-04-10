/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.perf.util;

import com.ibm.icu.util.Calendar;

/**
 * Time util class, provides two functions presently 1: return current time in
 * string format 2: return time span between two certan point of time in string
 * format
 */
public class TimeUtil {
	/** single instance */
	public static TimeUtil instance = new TimeUtil();

	/**
	 * Return string format of current time
	 * 
	 * @return current time
	 */
	public String getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return getTimeStr(calendar);
	}

	/**
	 * Format time string
	 * 
	 * @param calendar
	 * @return current time, format is: xx(h):xx(m):xx(s):xxx(ms)
	 */
	private String getTimeStr(Calendar calendar) {
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int milliSecond = calendar.get(Calendar.MILLISECOND);

		return hour + "(h):" + minute + "(m):" + second + "(s):" + milliSecond + "(ms)";
	}

	/**
	 * @return Time instance of current time point
	 */
	public TimePoint getTimePoint() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return new TimePoint(calendar);
	}

	/**
	 * @param time1
	 * @param time2
	 * @return time span between time1 and time2, computed approach is the value of
	 *         time2 subtracts that of time1
	 */
	public long getTimePointSpan(TimePoint time1, TimePoint time2) {
		return time2.getCalendar().getTimeInMillis() - time1.getCalendar().getTimeInMillis();
	}

	/**
	 * @param time1
	 * @param time2
	 * @return time span string between time1 and time2, computed approach is the
	 *         value of time2 subtracts that of time1
	 */
	public String getTimePointSpanStr(TimePoint time1, TimePoint time2) {
		return getTimePointSpanStr(time2.getCalendar().getTimeInMillis() - time1.getCalendar().getTimeInMillis());
	}

	/**
	 * Format milliSecondSpan to string
	 * 
	 * @param milliSecondSpan
	 * @return formatted string
	 */
	public String getTimePointSpanStr(long milliSecondSpan) {
		int adjustValue = 1;

		if (milliSecondSpan < 0) {
			milliSecondSpan *= -1;
			adjustValue = -1;
		}

		int hour = (int) (milliSecondSpan / (1000 * 60 * 60));
		int minute = (int) ((milliSecondSpan - hour * 1000 * 60 * 60) / (1000 * 60));
		int second = (int) ((milliSecondSpan - hour * 1000 * 60 * 60 - minute * 1000 * 60) / 1000);
		int milliSecond = (int) (milliSecondSpan - hour * 1000 * 60 * 60 - minute * 1000 * 60 - second * 1000);

		String str = hour + "(h):" + minute + "(m):" + second + "(s):" + milliSecond + "(ms)";
		if (adjustValue < 0)
			str = "- " + str;

		return str;
	}

	/**
	 * Store calendar at a certain time point
	 */
	public class TimePoint {
		/** stored calendar */
		private Calendar calendar;

		/**
		 * Construction
		 * 
		 * @param calendar
		 */
		private TimePoint(Calendar calendar) {
			assert calendar != null;
			this.calendar = calendar;
		}

		/**
		 * @return stored calendar
		 */
		private Calendar getCalendar() {
			return calendar;
		}
	}

}
