/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.adapter.impl;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.timeFunction.DateTimeUtility;
import org.eclipse.birt.report.data.adapter.group.ICalculator;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * This is an implementation of IDatasetIterator interface.
 *
 */
public class DataSetIterator implements IDatasetIterator {

	static final String DATE_TIME_ATTR_NAME = "DateTime";
	//
	private boolean started = false;
	private IResultIterator it;
	private ResultMeta metadata;
	private Calendar calendar;
	private Calendar defaultCalendar;
	private Calendar gmtCalendar;
	private SecurityListener securityListener;
	private long nullTime;
	private String dimName;

	private IQueryResults queryResult;

	/**
	 * 
	 * @param session
	 * @param query
	 * @param appContext
	 * @throws AdapterException
	 */
	private void executeQuery(DataRequestSessionImpl session, IQueryDefinition query, Map appContext)
			throws AdapterException {
		try {

			Scriptable scope = session.getScope();
			TempDateTransformer tt = new TempDateTransformer(
					session.getDataSessionContext().getDataEngineContext().getLocale());
			ScriptableObject.putProperty(scope, tt.getClassName(), tt);

			queryResult = session.prepare(query, appContext).execute(scope);
			this.it = queryResult.getResultIterator();
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Create DataSetIterator for fact table.
	 * 
	 * @param session
	 * @param cubeHandle
	 * @throws BirtException
	 */
	public DataSetIterator(DataRequestSessionImpl session, IQueryDefinition query, List<ColumnMeta> meta,
			Map appContext) throws BirtException {
		this.defaultCalendar = Calendar.getInstance(session.getDataSessionContext().getDataEngineContext().getLocale());
		this.defaultCalendar.setTimeZone(TimeZone.getDefault());
		this.defaultCalendar.clear();

		this.calendar = Calendar.getInstance(session.getDataSessionContext().getDataEngineContext().getLocale());
		this.calendar.setTimeZone(session.getDataSessionContext().getDataEngineContext().getTimeZone());
		this.calendar.clear();

		this.calendar.set(0, 0, 1, 0, 0, 0);
		this.nullTime = this.calendar.getTimeInMillis();
		this.calendar.clear();
		executeQuery(session, query, appContext);
		this.metadata = new ResultMeta(meta);

	}

	public static String createLevelACLName(String levelName) {
		return "_$ACL$_" + levelName;
	}

	public void initSecurityListenerAndDimension(String dimName, SecurityListener listener) {
		this.securityListener = listener;
		this.dimName = dimName;
	}

	static int getDefaultStartValue(String timeType, String value) throws AdapterException {
		if (value != null && Double.valueOf(value).doubleValue() != 0)
			return Integer.valueOf(value).intValue();
		if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals(timeType)) {
			return 1;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals(timeType)) {
			return 0;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals(timeType)) {
			return 0;
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals(timeType)) {
			return 0;
		} else
			throw new AdapterException("Error");
	}

	static String createLevelName(String dimName, String levelName) {
		if (dimName != null)
			return dimName + "/" + levelName;
		else
			return levelName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#close()
	 */
	public void close() throws BirtException {
		if (this.queryResult != null)
			this.queryResult.close();
		if (it != null)
			it.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getFieldIndex(
	 * java.lang.String)
	 */
	public int getFieldIndex(String name) throws BirtException {
		return this.metadata.getFieldIndex(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getFieldType(java
	 * .lang.String)
	 */
	public int getFieldType(String name) throws BirtException {
		return this.metadata.getFieldType(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#getValue(int)
	 */
	public Object getValue(int fieldIndex) throws BirtException {
		Object value = it.getValue(this.metadata.getFieldName(fieldIndex));
		if (value == null) {
			return this.metadata.getNullValueReplacer(fieldIndex);
		}
		if (byte[].class.equals(value.getClass())) {
			return value;
		}

		Object convertedValue = this.metadata.getDataProcessor(fieldIndex).process(value);

		return convertedValue.getClass().isAssignableFrom(value.getClass()) ? convertedValue
				: DataTypeUtil.convert(convertedValue, value.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator#next()
	 */
	public boolean next() throws BirtException {
		boolean hasNext = false;

		if (it.getQueryResults().getPreparedQuery().getReportQueryDefn().getGroups().size() == 0) {
			hasNext = it.next();
		} else if (!started) {
			started = true;
			hasNext = it.next();
		} else {
			it.skipToEnd(it.getQueryResults().getPreparedQuery().getReportQueryDefn().getGroups().size());
			hasNext = it.next();
		}

		if (!hasNext) {
			it.close();
		} else {
			if (this.securityListener != null)
				this.securityListener.process(dimName, this);
		}
		return hasNext;
	}

	public int getSummaryInt(String bindingName) throws BirtException {
		return it.getInteger(bindingName);
	}

	private Calendar getCalendar(Object d) {
		assert d != null;

		if (d instanceof java.sql.Date) {
			return this.defaultCalendar;
		}
		return this.calendar;
	}

	/**
	 * 
	 *
	 */
	private class ResultMeta {
		//
		private HashMap columnMetaMap;
		private HashMap indexMap;
		private Object[] nullValueReplacer;

		/**
		 * Constructor.
		 * 
		 * @param columnMetas
		 */
		ResultMeta(List columnMetas) {
			this.columnMetaMap = new HashMap();
			this.indexMap = new HashMap();
			this.nullValueReplacer = new Object[columnMetas.size()];
			for (int i = 0; i < columnMetas.size(); i++) {
				ColumnMeta columnMeta = (ColumnMeta) columnMetas.get(i);
				columnMeta.setIndex(i + 1);
				this.columnMetaMap.put(columnMeta.getName(), columnMeta);
				this.indexMap.put(Integer.valueOf(i + 1), columnMeta);
				if (columnMeta.isLevelKey()) {
					this.nullValueReplacer[i] = createNullValueReplacer(columnMeta.getType());
				}
			}
		}

		/**
		 * 
		 * @param fieldName
		 * @return
		 */
		public int getFieldIndex(String fieldName) {
			return ((ColumnMeta) this.columnMetaMap.get(fieldName)).getIndex();
		}

		/**
		 * 
		 * @param fieldName
		 * @return
		 */
		public int getFieldType(String fieldName) {
			return ((ColumnMeta) this.columnMetaMap.get(fieldName)).getType();
		}

		/**
		 * 
		 * @param index
		 * @return
		 */
		public String getFieldName(int index) {
			return ((ColumnMeta) this.indexMap.get(Integer.valueOf(index))).getName();
		}

		/**
		 * 
		 * @param index
		 * @return
		 */
		public Object getNullValueReplacer(int index) {
			return this.nullValueReplacer[index - 1];
		}

		public IDataProcessor getDataProcessor(int index) {
			return ((ColumnMeta) this.indexMap.get(Integer.valueOf(index))).getDataProcessor();
		}

		/**
		 * 
		 * @param fieldType
		 * @return
		 */
		private Object createNullValueReplacer(int fieldType) {

			switch (fieldType) {
			case DataType.DATE_TYPE:
				return new java.util.Date(nullTime);
			case DataType.SQL_DATE_TYPE:
				return new java.sql.Date(nullTime);
			case DataType.SQL_TIME_TYPE:
				return new Time(nullTime);
			case DataType.BOOLEAN_TYPE:
				return Boolean.FALSE;
			case DataType.DECIMAL_TYPE:
				return new BigDecimal(0);
			case DataType.DOUBLE_TYPE:
				return new Double(0);
			case DataType.INTEGER_TYPE:
				return Integer.valueOf(0);
			case DataType.STRING_TYPE:
				return "";
			default:
				return "";
			}
		}
	}

	/**
	 * 
	 *
	 */
	static class ColumnMeta {
		//
		static final int LEVEL_KEY_TYPE = 1;
		static final int MEASURE_TYPE = 2;
		static final int UNKNOWN_TYPE = 3;

		private String name;
		private int dataType, type;
		private int index;
		private IDataProcessor dataProcessor;

		/**
		 * 
		 * @param name
		 */
		ColumnMeta(String name, IDataProcessor processor, int type) {
			this.name = name;
			this.type = type;
			this.dataProcessor = (processor == null) ? new DummyDataProcessor() : processor;
		}

		/**
		 * 
		 * @return
		 */
		public int getIndex() {
			return this.index;
		}

		/**
		 * 
		 * @return
		 */
		public int getType() {
			return this.dataType;
		}

		/**
		 * 
		 * @return
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * 
		 * @param index
		 */
		public void setIndex(int index) {
			this.index = index;
		}

		/**
		 * 
		 * @param type
		 */
		public void setDataType(int type) {
			this.dataType = type;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isLevelKey() {
			return this.type == LEVEL_KEY_TYPE;
		}

		/**
		 * 
		 * @return
		 */
		public boolean isMeasure() {
			return this.type == MEASURE_TYPE;
		}

		/**
		 * 
		 * @param type
		 */
		public void setType(int type) {
			this.type = type;
		}

		/**
		 * 
		 * @return
		 */
		public IDataProcessor getDataProcessor() {
			return this.dataProcessor;
		}
	}

	interface IDataProcessor {
		public Object process(Object d) throws AdapterException;
	}

	private static class DummyDataProcessor implements IDataProcessor {
		public Object process(Object d) {
			return d;
		}
	}

	/**
	 * Clear time portion of a date value
	 * 
	 * @param d
	 */
	private static void cleanTimePortion(Calendar d) {
		d.set(Calendar.HOUR_OF_DAY, 0);
		d.set(Calendar.MINUTE, 0);
		d.set(Calendar.SECOND, 0);
		d.set(Calendar.MILLISECOND, 0);
	}

	static String createDateTransformerExpr(String timeType, String value) {
		return "TempDateTransformer.transform(\"" + timeType + "\"," + value + ")";

	}

	private class TempDateTransformer extends ScriptableObject {

		private static final long serialVersionUID = 1L;

		public TempDateTransformer(ULocale locale) {
			this.defineProperty("transform", new Function_Transform(locale), 0);
		}

		/**
		 * 
		 */
		public String getClassName() {
			return "TempDateTransformer";
		}

	}

	private class Function_Transform extends Function_temp {
		private static final long serialVersionUID = 1L;
		private ULocale locale;

		public Function_Transform(ULocale locale) {
			this.locale = locale;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.data.adapter.impl.DataSetIterator.TimeValueProcessor.
		 * Function_temp#getValue(java.lang.Object[])
		 */
		protected Object getValue(Object[] args) throws BirtException {
			assert args.length == 2;
			String timeType = args[0].toString();
			Object d = args[1];

			return DateTimeUtility.getPortion(d, timeType, getCalendar(d));
		}
	}

	abstract class Function_temp extends BaseFunction implements Function {

		private static final long serialVersionUID = 1L;

		public Object call(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) {
			args = convertToJavaObjects(args);

			try {
				return getValue(args);
			} catch (BirtException e) {
				throw new IllegalArgumentException("The type of arguement is incorrect.");
			}
		}

		protected abstract Object getValue(Object[] args) throws BirtException;

		private Object[] convertToJavaObjects(Object[] args) {
			for (int i = 0; i < args.length; i++) {
				args[i] = JavascriptEvalUtil.convertJavascriptValue(args[i]);
			}
			return args;
		}

	}

	static class DataProcessorWrapper implements IDataProcessor {
		private ICalculator calculator;

		DataProcessorWrapper(ICalculator calculator) {
			this.calculator = calculator;
		}

		public Object process(Object d) throws AdapterException {
			try {
				return this.calculator.calculate(d);
			} catch (BirtException e) {
				throw new AdapterException(e.getLocalizedMessage(), e);
			}
		}

	}

	/**
	 * For all Time level, there is by default an "DateTime" attribute which
	 * contains the corresponding DateTime value of that time level.
	 */
	static class DateTimeAttributeProcessor implements IDataProcessor {
		private String timeType;
		private Calendar calendar;

		DateTimeAttributeProcessor(String timeType, ULocale locale, TimeZone zone) {
			this.timeType = timeType;
			this.calendar = Calendar.getInstance(locale);
			if (zone != null)
				this.calendar.setTimeZone(zone);
		}

		private void populateCalendar(Object d) {
			assert d != null;

			Date date;
			try {
				date = DataTypeUtil.toDate(d);
				this.calendar.setTime(date);
			} catch (BirtException e) {
				throw new java.lang.IllegalArgumentException(
						AdapterResourceHandle.getInstance().getMessage(ResourceConstants.INVALID_DATETIME_VALUE));
			}
		}

		public Object process(Object d) throws AdapterException {
			if (d == null)
				return null;

			populateCalendar(d);
			if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(timeType)
					|| DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(timeType)
					|| DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(timeType)
					|| DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(timeType)
					|| DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(timeType)) {
				cleanTimePortion(this.calendar);
				return this.calendar.getTime();
			} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals(timeType)) {
				// For all month, clear time portion and set DayOfMonth to 1.
				cleanTimePortion(this.calendar);
				this.calendar.set(Calendar.DATE, 1);
				return this.calendar.getTime();
			} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals(timeType)) {
				// For all quarter, clear time portion and set month to first month of
				// that quarter and set day to first day of that month.
				cleanTimePortion(this.calendar);
				this.calendar.set(Calendar.DATE, 1);
				int month = this.calendar.get(Calendar.MONTH);
				switch (month) {
				case Calendar.JANUARY:
				case Calendar.FEBRUARY:
				case Calendar.MARCH:
					this.calendar.set(Calendar.MONTH, 0);
					break;
				case Calendar.APRIL:
				case Calendar.MAY:
				case Calendar.JUNE:
					this.calendar.set(Calendar.MONTH, 3);
					break;
				case Calendar.JULY:
				case Calendar.AUGUST:
				case Calendar.SEPTEMBER:
					this.calendar.set(Calendar.MONTH, 6);
					break;
				case Calendar.OCTOBER:
				case Calendar.NOVEMBER:
				case Calendar.DECEMBER:
					this.calendar.set(Calendar.MONTH, 9);
					break;
				}
				return this.calendar.getTime();
			} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals(timeType)) {
				// For year, clear all time portion and set Date to Jan 1st.
				cleanTimePortion(this.calendar);
				this.calendar.set(Calendar.MONTH, 0);
				this.calendar.set(Calendar.DATE, 1);
				return this.calendar.getTime();
			} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals(timeType)) {
				// For hour, set minute to 0 and second to 1.
				this.calendar.set(Calendar.MINUTE, 0);
				this.calendar.set(Calendar.SECOND, 1);
				this.calendar.set(Calendar.MILLISECOND, 0);
				return this.calendar.getTime();
			} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals(timeType)) {
				// For minute, set second to 1.
				this.calendar.set(Calendar.SECOND, 1);
				this.calendar.set(Calendar.MILLISECOND, 0);
				return this.calendar.getTime();
			} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals(timeType)) {
				// For second, set millisecond to 0.
				this.calendar.set(Calendar.MILLISECOND, 0);
				return this.calendar.getTime();
			} else
				throw new AdapterException(ResourceConstants.INVALID_DATE_TIME_TYPE, timeType);

		}
	}
}
