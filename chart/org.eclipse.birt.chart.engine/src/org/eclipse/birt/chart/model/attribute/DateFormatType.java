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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Date Format Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * This type defines predefined date format types.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDateFormatType()
 * @model
 * @generated
 */
public enum DateFormatType implements Enumerator {
	/**
	 * The '<em><b>Long</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Long</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #LONG
	 * @generated
	 * @ordered
	 */
	LONG_LITERAL(0, "Long", "Long"),
	/**
	 * The '<em><b>Short</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Short</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #SHORT
	 * @generated
	 * @ordered
	 */
	SHORT_LITERAL(1, "Short", "Short"),
	/**
	 * The '<em><b>Medium</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Medium</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MEDIUM
	 * @generated
	 * @ordered
	 */
	MEDIUM_LITERAL(2, "Medium", "Medium"),
	/**
	 * The '<em><b>Full</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Full</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #FULL
	 * @generated
	 * @ordered
	 */
	FULL_LITERAL(3, "Full", "Full");

	/**
	 * The '<em><b>Long</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #LONG_LITERAL
	 * @model name="Long"
	 * @generated
	 * @ordered
	 */
	public static final int LONG = 0;

	/**
	 * The '<em><b>Short</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #SHORT_LITERAL
	 * @model name="Short"
	 * @generated
	 * @ordered
	 */
	public static final int SHORT = 1;

	/**
	 * The '<em><b>Medium</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #MEDIUM_LITERAL
	 * @model name="Medium"
	 * @generated
	 * @ordered
	 */
	public static final int MEDIUM = 2;

	/**
	 * The '<em><b>Full</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #FULL_LITERAL
	 * @model name="Full"
	 * @generated
	 * @ordered
	 */
	public static final int FULL = 3;

	/**
	 * An array of all the '<em><b>Date Format Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final DateFormatType[] VALUES_ARRAY = { LONG_LITERAL, SHORT_LITERAL, MEDIUM_LITERAL, FULL_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Date Format Type</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<DateFormatType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Date Format Type</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static DateFormatType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DateFormatType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Date Format Type</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static DateFormatType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DateFormatType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Date Format Type</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static DateFormatType get(int value) {
		switch (value) {
		case LONG:
			return LONG_LITERAL;
		case SHORT:
			return SHORT_LITERAL;
		case MEDIUM:
			return MEDIUM_LITERAL;
		case FULL:
			return FULL_LITERAL;
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
	DateFormatType(int value, String name, String literal) {
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
}
