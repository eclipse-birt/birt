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
 * $Id$
 */

package org.eclipse.birt.chart.model.attribute;

import com.ibm.icu.util.ULocale;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>String
 * Format Specifier</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> StringFormatSpecifier extends type FormatSpecifier
 * to devote itself to formatting a string value.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.StringFormatSpecifier#getPattern
 * <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStringFormatSpecifier()
 * @model extendedMetaData="name='StringFormatSpecifier' kind='elementOnly'"
 * @generated
 */
public interface StringFormatSpecifier extends FormatSpecifier {

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Pattern" specifies the format pattern. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Pattern</em>' attribute.
	 * @see #setPattern(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getStringFormatSpecifier_Pattern()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Pattern'"
	 * @generated
	 */
	String getPattern();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.StringFormatSpecifier#getPattern
	 * <em>Pattern</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Pattern</em>' attribute.
	 * @see #getPattern()
	 * @generated
	 */
	void setPattern(String value);

	/**
	 * @generated
	 */
	@Override
	StringFormatSpecifier copyInstance();

	/**
	 * Formats a value using the internally defined format specifier rules
	 *
	 * @param dValue
	 *
	 * @return A formatted string representation of the string value provided
	 */
	String format(String dValue, ULocale lo);

} // StringFormatSpecifier
