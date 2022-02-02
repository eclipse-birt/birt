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
 * $Id: UnitsOfMeasurement.java,v 1.5 2007/02/02 03:15:57 yulin Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Units Of Measurement</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values of Units of Measurement.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getUnitsOfMeasurement()
 * @model
 * @generated
 */
public enum UnitsOfMeasurement implements Enumerator {
	/**
	 * The '<em><b>Pixels</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Pixels</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #PIXELS
	 * @generated
	 * @ordered
	 */
	PIXELS_LITERAL(0, "Pixels", "Pixels"),
	/**
	 * The '<em><b>Points</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Points</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #POINTS
	 * @generated
	 * @ordered
	 */
	POINTS_LITERAL(1, "Points", "Points"),
	/**
	 * The '<em><b>Inches</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Inches</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #INCHES
	 * @generated
	 * @ordered
	 */
	INCHES_LITERAL(2, "Inches", "Inches"),
	/**
	 * The '<em><b>Centimeters</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Centimeters</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CENTIMETERS
	 * @generated
	 * @ordered
	 */
	CENTIMETERS_LITERAL(3, "Centimeters", "Centimeters");

	/**
	 * The '<em><b>Pixels</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #PIXELS_LITERAL
	 * @model name="Pixels"
	 * @generated
	 * @ordered
	 */
	public static final int PIXELS = 0;

	/**
	 * The '<em><b>Points</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #POINTS_LITERAL
	 * @model name="Points"
	 * @generated
	 * @ordered
	 */
	public static final int POINTS = 1;

	/**
	 * The '<em><b>Inches</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #INCHES_LITERAL
	 * @model name="Inches"
	 * @generated
	 * @ordered
	 */
	public static final int INCHES = 2;

	/**
	 * The '<em><b>Centimeters</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CENTIMETERS_LITERAL
	 * @model name="Centimeters"
	 * @generated
	 * @ordered
	 */
	public static final int CENTIMETERS = 3;

	/**
	 * An array of all the '<em><b>Units Of Measurement</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final UnitsOfMeasurement[] VALUES_ARRAY = new UnitsOfMeasurement[] { PIXELS_LITERAL, POINTS_LITERAL,
			INCHES_LITERAL, CENTIMETERS_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Units Of Measurement</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<UnitsOfMeasurement> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Units Of Measurement</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static UnitsOfMeasurement get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			UnitsOfMeasurement result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Units Of Measurement</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static UnitsOfMeasurement getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			UnitsOfMeasurement result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Units Of Measurement</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static UnitsOfMeasurement get(int value) {
		switch (value) {
		case PIXELS:
			return PIXELS_LITERAL;
		case POINTS:
			return POINTS_LITERAL;
		case INCHES:
			return INCHES_LITERAL;
		case CENTIMETERS:
			return CENTIMETERS_LITERAL;
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
	private UnitsOfMeasurement(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public int getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
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
