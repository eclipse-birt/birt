package org.eclipse.birt.report.data.adapter.impl;

import java.util.Date;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.Calendar;

public class TimeDimensionDatasetIterator implements IDatasetIterator {
	private Calendar calendar;
	private Date endTime;
	private boolean firstNext;
	private String[] fieldName;
	private String[] timeType;

	TimeDimensionDatasetIterator(DataRequestSessionImpl session, Date startTime, Date endTime, String[] fieldName,
			String[] timeType) throws AdapterException {
		try {
			if (startTime != null) {
				this.calendar = Calendar
						.getInstance(session.getDataSessionContext().getDataEngineContext().getLocale());
				this.calendar.setTimeZone(session.getDataSessionContext().getDataEngineContext().getTimeZone());
				this.calendar.clear();
				this.calendar.setTime(startTime);
			}

			this.endTime = endTime;
			this.fieldName = fieldName;
			this.timeType = timeType;
			this.firstNext = false;
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 
	 * @param timeType
	 * @param d
	 * @return
	 * @throws BirtException
	 */
	protected Object getValue(String timeType, Object d) throws BirtException {
		if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.DAY_OF_MONTH));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.DAY_OF_WEEK));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.DAY_OF_YEAR));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.WEEK_OF_MONTH));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.WEEK_OF_YEAR));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.MONTH) + 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals(timeType)) {
			int month = getCalendar(d).get(Calendar.MONTH);
			int quarter = -1;
			switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				quarter = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				quarter = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				quarter = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				quarter = 4;
				break;
			default:
				quarter = -1;
			}
			return new Integer(quarter);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.YEAR));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.HOUR_OF_DAY));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals(timeType)) {
			return new Integer(getCalendar(d).get(Calendar.MINUTE));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals(timeType)) {
			return Integer.valueOf(getCalendar(d).get(Calendar.SECOND));
		} else
			throw new AdapterException(ResourceConstants.INVALID_DATE_TIME_TYPE, timeType);
	}

	/**
	 * 
	 * @param d
	 * @return
	 */
	private Calendar getCalendar(Object d) {
		assert d != null;

		Date date;
		try {
			date = DataTypeUtil.toDate(d);
			this.calendar.setTime(date);
			return this.calendar;
		} catch (BirtException e) {
			throw new java.lang.IllegalArgumentException(
					AdapterResourceHandle.getInstance().getMessage(ResourceConstants.INVALID_DATETIME_VALUE));
		}
	}

	public void close() throws BirtException {

	}

	public int getFieldIndex(String name) throws BirtException {
		for (int i = 0; i < fieldName.length; i++) {
			if (fieldName[i].equals(name))
				return i;
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		for (int i = 0; i < fieldName.length; i++) {
			if (fieldName[i].equals(name))
				return DataType.INTEGER_TYPE;
		}
		return DataType.DATE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator#getValue(
	 * int)
	 */
	public Object getValue(int fieldIndex) throws BirtException {
		if (fieldIndex == -1)
			return calendar.getTime();
		return getValue(timeType[fieldIndex], calendar.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator#next()
	 */
	public boolean next() throws BirtException {
		if (calendar == null || endTime == null)
			return false;
		if (!firstNext) {
			firstNext = true;
			return true;
		}
		nextValue(timeType[timeType.length - 1]);
		if (endTime.compareTo(calendar.getTime()) < 0)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @param timeType
	 */
	private void nextValue(String timeType) {
		if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(timeType)) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(timeType)) {
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(timeType)) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(timeType)) {
			calendar.add(Calendar.WEEK_OF_MONTH, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(timeType)) {
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals(timeType)) {
			calendar.add(Calendar.MONTH, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals(timeType)) {
			calendar.add(Calendar.DAY_OF_MONTH, 3);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals(timeType)) {
			calendar.add(Calendar.YEAR, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals(timeType)) {
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals(timeType)) {
			calendar.add(Calendar.MINUTE, 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals(timeType)) {
			calendar.add(Calendar.SECOND, 1);
		}
	}

}
