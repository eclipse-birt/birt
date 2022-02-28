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
 * <em><b>Leader Line Style</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * This type defines the forms of leader lines.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLeaderLineStyle()
 * @model
 * @generated
 */
public enum LeaderLineStyle implements Enumerator {
	/**
	 * The '<em><b>Fixed Length</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Fixed Length</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #FIXED_LENGTH
	 * @generated
	 * @ordered
	 */
	FIXED_LENGTH_LITERAL(0, "FixedLength", "Fixed_Length"),
	/**
	 * The '<em><b>Stretch To Side</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Stretch To Side</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #STRETCH_TO_SIDE
	 * @generated
	 * @ordered
	 */
	STRETCH_TO_SIDE_LITERAL(1, "StretchToSide", "Stretch_To_Side");

	/**
	 * The '<em><b>Fixed Length</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #FIXED_LENGTH_LITERAL
	 * @model name="Fixed_Length"
	 * @generated
	 * @ordered
	 */
	public static final int FIXED_LENGTH = 0;

	/**
	 * The '<em><b>Stretch To Side</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #STRETCH_TO_SIDE_LITERAL
	 * @model name="Stretch_To_Side"
	 * @generated
	 * @ordered
	 */
	public static final int STRETCH_TO_SIDE = 1;

	/**
	 * An array of all the '<em><b>Leader Line Style</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final LeaderLineStyle[] VALUES_ARRAY = { FIXED_LENGTH_LITERAL, STRETCH_TO_SIDE_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Leader Line Style</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<LeaderLineStyle> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Leader Line Style</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static LeaderLineStyle get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LeaderLineStyle result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Leader Line Style</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static LeaderLineStyle getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LeaderLineStyle result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Leader Line Style</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static LeaderLineStyle get(int value) {
		switch (value) {
		case FIXED_LENGTH:
			return FIXED_LENGTH_LITERAL;
		case STRETCH_TO_SIDE:
			return STRETCH_TO_SIDE_LITERAL;
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
	LeaderLineStyle(int value, String name, String literal) {
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
