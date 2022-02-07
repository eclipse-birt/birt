/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.attribute;

import java.util.Locale;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Date
 * Format Specifier</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> DateFormatSpecifier extends the type FormatSpecifier
 * to devote itself to formating text output of a date/time value.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail
 * <em>Detail</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDateFormatSpecifier()
 * @model extendedMetaData="name='DateFormatSpecifier' kind='elementOnly'"
 * @generated
 */
public interface DateFormatSpecifier extends FormatSpecifier {

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The default value
	 * is <code>"Long"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.DateFormatType}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the format specifier.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(DateFormatType)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDateFormatSpecifier_Type()
	 * @model default="Long" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	DateFormatType getType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(DateFormatType value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType
	 * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(DateFormatType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getType
	 * <em>Type</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(DateFormatType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Returns the value of the '<em><b>Detail</b></em>' attribute. The default
	 * value is <code>"Date"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.DateFormatDetail}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Species the form of the date.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Detail</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @see #isSetDetail()
	 * @see #unsetDetail()
	 * @see #setDetail(DateFormatDetail)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDateFormatSpecifier_Detail()
	 * @model default="Date" unique="false" unsettable="true" required="true"
	 * @generated
	 */
	DateFormatDetail getDetail();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail
	 * <em>Detail</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Detail</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.DateFormatDetail
	 * @see #isSetDetail()
	 * @see #unsetDetail()
	 * @see #getDetail()
	 * @generated
	 */
	void setDetail(DateFormatDetail value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail
	 * <em>Detail</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetDetail()
	 * @see #getDetail()
	 * @see #setDetail(DateFormatDetail)
	 * @generated
	 */
	void unsetDetail();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.DateFormatSpecifier#getDetail
	 * <em>Detail</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Detail</em>' attribute is set.
	 * @see #unsetDetail()
	 * @see #getDetail()
	 * @see #setDetail(DateFormatDetail)
	 * @generated
	 */
	boolean isSetDetail();

	/**
	 * Formats a calendar value using the date format pattern
	 * 
	 * NOTE: Manually written
	 * 
	 * @param c
	 * @param lcl
	 * @deprecated
	 */
	String format(Calendar c, Locale lcl);

	/**
	 * Formats a calendar value using the date format pattern
	 * 
	 * NOTE: Manually written
	 * 
	 * @param c
	 * @param lcl
	 * @since 2.1
	 */
	String format(Calendar c, ULocale lcl);

	/**
	 * @generated
	 */
	DateFormatSpecifier copyInstance();

} // DateFormatSpecifier
