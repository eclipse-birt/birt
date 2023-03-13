/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id: GroupingUnitType.java,v 1.7 2007/12/24 07:43:33 yulin Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Grouping Unit Type</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * This type represents the possible units for grouping data.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getGroupingUnitType()
 * @model
 * @generated
 */
public enum GroupingUnitType implements Enumerator {
	/**
	 * The '<em><b>Seconds</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Seconds</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SECONDS
	 * @generated
	 * @ordered
	 */
	SECONDS_LITERAL(0, "Seconds", "Seconds"),
	/**
	 * The '<em><b>Minutes</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Minutes</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MINUTES
	 * @generated
	 * @ordered
	 */
	MINUTES_LITERAL(1, "Minutes", "Minutes"),
	/**
	 * The '<em><b>Hours</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Hours</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #HOURS
	 * @generated
	 * @ordered
	 */
	HOURS_LITERAL(2, "Hours", "Hours"),
	/**
	 * The '<em><b>Days</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Days</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #DAYS
	 * @generated
	 * @ordered
	 */
	DAYS_LITERAL(3, "Days", "Days"),
	/**
	 * The '<em><b>Weeks</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Weeks</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEKS
	 * @generated
	 * @ordered
	 */
	WEEKS_LITERAL(4, "Weeks", "Weeks"),
	/**
	 * The '<em><b>Months</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Months</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MONTHS
	 * @generated
	 * @ordered
	 */
	MONTHS_LITERAL(5, "Months", "Months"),
	/**
	 * The '<em><b>Quarters</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #QUARTERS
	 * @generated
	 * @ordered
	 */
	QUARTERS_LITERAL(6, "Quarters", "Quarters"),
	/**
	 * The '<em><b>Years</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Years</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #YEARS
	 * @generated
	 * @ordered
	 */
	YEARS_LITERAL(7, "Years", "Years"),
	/**
	 * The '<em><b>String</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #STRING
	 * @generated
	 * @ordered
	 */
	STRING_LITERAL(8, "String", "String"),
	/**
	 * The '<em><b>String Prefix</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #STRING_PREFIX
	 * @generated
	 * @ordered
	 */
	STRING_PREFIX_LITERAL(9, "StringPrefix", "StringPrefix"),
	/**
	 * The '<em><b>Week Of Month</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEK_OF_MONTH
	 * @generated
	 * @ordered
	 */
	WEEK_OF_MONTH_LITERAL(10, "WeekOfMonth", "WeekOfMonth"),
	/**
	 * The '<em><b>Week Of Year</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEK_OF_YEAR
	 * @generated
	 * @ordered
	 */
	WEEK_OF_YEAR_LITERAL(11, "WeekOfYear", "WeekOfYear"),
	/**
	 * The '<em><b>Day Of Week</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_WEEK
	 * @generated
	 * @ordered
	 */
	DAY_OF_WEEK_LITERAL(12, "DayOfWeek", "DayOfWeek"),
	/**
	 * The '<em><b>Day Of Month</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_MONTH
	 * @generated
	 * @ordered
	 */
	DAY_OF_MONTH_LITERAL(13, "DayOfMonth", "DayOfMonth"),
	/**
	 * The '<em><b>Day Of Year</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_YEAR
	 * @generated
	 * @ordered
	 */
	DAY_OF_YEAR_LITERAL(14, "DayOfYear", "DayOfYear"),
	/**
	 * The '<em><b>Week Of Quarter</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEK_OF_QUARTER
	 * @generated
	 * @ordered
	 */
	WEEK_OF_QUARTER_LITERAL(15, "WeekOfQuarter", "WeekOfQuarter"),
	/**
	 * The '<em><b>Day Of Quarter</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_QUARTER
	 * @generated
	 * @ordered
	 */
	DAY_OF_QUARTER_LITERAL(16, "DayOfQuarter", "DayOfQuarter");

	/**
	 * The '<em><b>Seconds</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #SECONDS_LITERAL
	 * @model name="Seconds"
	 * @generated
	 * @ordered
	 */
	public static final int SECONDS = 0;

	/**
	 * The '<em><b>Minutes</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #MINUTES_LITERAL
	 * @model name="Minutes"
	 * @generated
	 * @ordered
	 */
	public static final int MINUTES = 1;

	/**
	 * The '<em><b>Hours</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #HOURS_LITERAL
	 * @model name="Hours"
	 * @generated
	 * @ordered
	 */
	public static final int HOURS = 2;

	/**
	 * The '<em><b>Days</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #DAYS_LITERAL
	 * @model name="Days"
	 * @generated
	 * @ordered
	 */
	public static final int DAYS = 3;

	/**
	 * The '<em><b>Weeks</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #WEEKS_LITERAL
	 * @model name="Weeks"
	 * @generated
	 * @ordered
	 */
	public static final int WEEKS = 4;

	/**
	 * The '<em><b>Months</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #MONTHS_LITERAL
	 * @model name="Months"
	 * @generated
	 * @ordered
	 */
	public static final int MONTHS = 5;

	/**
	 * The '<em><b>Quarters</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Quarters</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #QUARTERS_LITERAL
	 * @model name="Quarters"
	 * @generated
	 * @ordered
	 */
	public static final int QUARTERS = 6;

	/**
	 * The '<em><b>Years</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #YEARS_LITERAL
	 * @model name="Years"
	 * @generated
	 * @ordered
	 */
	public static final int YEARS = 7;

	/**
	 * The '<em><b>String</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>String</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #STRING_LITERAL
	 * @model name="String"
	 * @generated
	 * @ordered
	 */
	public static final int STRING = 8;

	/**
	 * The '<em><b>String Prefix</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>String Prefix</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #STRING_PREFIX_LITERAL
	 * @model name="StringPrefix"
	 * @generated
	 * @ordered
	 */
	public static final int STRING_PREFIX = 9;

	/**
	 * The '<em><b>Week Of Month</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Week Of Month</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEK_OF_MONTH_LITERAL
	 * @model name="WeekOfMonth"
	 * @generated
	 * @ordered
	 */
	public static final int WEEK_OF_MONTH = 10;

	/**
	 * The '<em><b>Week Of Year</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Week Of Year</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEK_OF_YEAR_LITERAL
	 * @model name="WeekOfYear"
	 * @generated
	 * @ordered
	 */
	public static final int WEEK_OF_YEAR = 11;

	/**
	 * The '<em><b>Day Of Week</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Day Of Week</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_WEEK_LITERAL
	 * @model name="DayOfWeek"
	 * @generated
	 * @ordered
	 */
	public static final int DAY_OF_WEEK = 12;

	/**
	 * The '<em><b>Day Of Month</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Day Of Month</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_MONTH_LITERAL
	 * @model name="DayOfMonth"
	 * @generated
	 * @ordered
	 */
	public static final int DAY_OF_MONTH = 13;

	/**
	 * The '<em><b>Day Of Year</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Day Of Year</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_YEAR_LITERAL
	 * @model name="DayOfYear"
	 * @generated
	 * @ordered
	 */
	public static final int DAY_OF_YEAR = 14;

	/**
	 * The '<em><b>Week Of Quarter</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Week Of Quarter</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #WEEK_OF_QUARTER_LITERAL
	 * @model name="WeekOfQuarter"
	 * @generated
	 * @ordered
	 */
	public static final int WEEK_OF_QUARTER = 15;

	/**
	 * The '<em><b>Day Of Quarter</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Day Of Quarter</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #DAY_OF_QUARTER_LITERAL
	 * @model name="DayOfQuarter"
	 * @generated
	 * @ordered
	 */
	public static final int DAY_OF_QUARTER = 16;

	/**
	 * An array of all the '<em><b>Grouping Unit Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final GroupingUnitType[] VALUES_ARRAY = { SECONDS_LITERAL, MINUTES_LITERAL, HOURS_LITERAL,
			DAYS_LITERAL, WEEKS_LITERAL, MONTHS_LITERAL, QUARTERS_LITERAL, YEARS_LITERAL, STRING_LITERAL,
			STRING_PREFIX_LITERAL, WEEK_OF_MONTH_LITERAL, WEEK_OF_YEAR_LITERAL, DAY_OF_WEEK_LITERAL,
			DAY_OF_MONTH_LITERAL, DAY_OF_YEAR_LITERAL, WEEK_OF_QUARTER_LITERAL, DAY_OF_QUARTER_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Grouping Unit Type</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<GroupingUnitType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Grouping Unit Type</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static GroupingUnitType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			GroupingUnitType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Grouping Unit Type</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static GroupingUnitType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			GroupingUnitType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Grouping Unit Type</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static GroupingUnitType get(int value) {
		switch (value) {
		case SECONDS:
			return SECONDS_LITERAL;
		case MINUTES:
			return MINUTES_LITERAL;
		case HOURS:
			return HOURS_LITERAL;
		case DAYS:
			return DAYS_LITERAL;
		case WEEKS:
			return WEEKS_LITERAL;
		case MONTHS:
			return MONTHS_LITERAL;
		case QUARTERS:
			return QUARTERS_LITERAL;
		case YEARS:
			return YEARS_LITERAL;
		case STRING:
			return STRING_LITERAL;
		case STRING_PREFIX:
			return STRING_PREFIX_LITERAL;
		case WEEK_OF_MONTH:
			return WEEK_OF_MONTH_LITERAL;
		case WEEK_OF_YEAR:
			return WEEK_OF_YEAR_LITERAL;
		case DAY_OF_WEEK:
			return DAY_OF_WEEK_LITERAL;
		case DAY_OF_MONTH:
			return DAY_OF_MONTH_LITERAL;
		case DAY_OF_YEAR:
			return DAY_OF_YEAR_LITERAL;
		case WEEK_OF_QUARTER:
			return WEEK_OF_QUARTER_LITERAL;
		case DAY_OF_QUARTER:
			return DAY_OF_QUARTER_LITERAL;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @generated
	 */
	GroupingUnitType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getLiteral() {
		return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string
	 * representation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}

	/**
	 * Returns grouping units type by specified data type.
	 *
	 * @param dataType
	 * @return
	 * @since BIRT 2.3
	 */
	public static List getGroupingUnits(DataType dataType) {
		if (dataType == DataType.NUMERIC_LITERAL) {
			return null;
		} else if (dataType == DataType.DATE_TIME_LITERAL) {
			List valuesList = new ArrayList(YEARS);
			for (int i = 0; i <= YEARS; i++) {
				valuesList.add(VALUES_ARRAY[i]);
			}
			return valuesList;
		} else if (dataType == DataType.TEXT_LITERAL) {
			List valuesList = new ArrayList();
			for (int i = STRING; i <= STRING_PREFIX; i++) {
				valuesList.add(VALUES_ARRAY[i]);
			}
			return valuesList;
		}

		return null;
	}
}
