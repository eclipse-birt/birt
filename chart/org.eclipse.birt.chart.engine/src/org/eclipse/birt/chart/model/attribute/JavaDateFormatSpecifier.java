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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Java
 * Date Format Specifier</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> JavaDateFormatSpecifier extends FormatSpecifier
 * specialized to represent a java DateFormat instance.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern
 * <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaDateFormatSpecifier()
 * @model extendedMetaData="name='JavaDateFormatSpecifier' kind='elementOnly'"
 * @generated
 */
public interface JavaDateFormatSpecifier extends FormatSpecifier {

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Pattern" specifies the pattern string used for establishing a DateFormat
	 * instance.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Pattern</em>' attribute.
	 * @see #setPattern(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getJavaDateFormatSpecifier_Pattern()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Pattern'"
	 * @generated
	 */
	String getPattern();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier#getPattern
	 * <em>Pattern</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Pattern</em>' attribute.
	 * @see #getPattern()
	 * @generated
	 */
	void setPattern(String value);

	/**
	 * Formats a calendar value using the Java date format pattern
	 *
	 * NOTE: Manually written
	 *
	 * @param c
	 * @param lcl
	 * @deprecated
	 */
	@Deprecated
	String format(Calendar c, Locale lcl);

	/**
	 * Formats a calendar value using the Java date format pattern
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
	@Override
	JavaDateFormatSpecifier copyInstance();

} // JavaDateFormatSpecifier
