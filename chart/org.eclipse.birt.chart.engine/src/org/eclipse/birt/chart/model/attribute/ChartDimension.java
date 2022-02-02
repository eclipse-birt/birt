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
 * <em><b>Chart Dimension</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for Chart dimensions. Any new Chart
 * dimension type needs to be added here to be supported.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getChartDimension()
 * @model
 * @generated
 */
public enum ChartDimension implements Enumerator {
	/**
	 * The '<em><b>Two Dimensional</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Two Dimensional</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL
	 * @generated
	 * @ordered
	 */
	TWO_DIMENSIONAL_LITERAL(0, "TwoDimensional", "Two_Dimensional"),
	/**
	 * The '<em><b>Two Dimensional With Depth</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Two Dimensional With Depth</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL_WITH_DEPTH
	 * @generated
	 * @ordered
	 */
	TWO_DIMENSIONAL_WITH_DEPTH_LITERAL(1, "TwoDimensionalWithDepth", "Two_Dimensional_With_Depth"),
	/**
	 * The '<em><b>Three Dimensional</b></em>' literal object. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Three Dimensional</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #THREE_DIMENSIONAL
	 * @generated
	 * @ordered
	 */
	THREE_DIMENSIONAL_LITERAL(2, "ThreeDimensional", "Three_Dimensional");

	/**
	 * The '<em><b>Two Dimensional</b></em>' literal value. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL_LITERAL
	 * @model name="Two_Dimensional"
	 * @generated
	 * @ordered
	 */
	public static final int TWO_DIMENSIONAL = 0;

	/**
	 * The '<em><b>Two Dimensional With Depth</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL_WITH_DEPTH_LITERAL
	 * @model name="Two_Dimensional_With_Depth"
	 * @generated
	 * @ordered
	 */
	public static final int TWO_DIMENSIONAL_WITH_DEPTH = 1;

	/**
	 * The '<em><b>Three Dimensional</b></em>' literal value. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #THREE_DIMENSIONAL_LITERAL
	 * @model name="Three_Dimensional"
	 * @generated
	 * @ordered
	 */
	public static final int THREE_DIMENSIONAL = 2;

	/**
	 * An array of all the '<em><b>Chart Dimension</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ChartDimension[] VALUES_ARRAY = new ChartDimension[] { TWO_DIMENSIONAL_LITERAL,
			TWO_DIMENSIONAL_WITH_DEPTH_LITERAL, THREE_DIMENSIONAL_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Chart Dimension</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<ChartDimension> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Chart Dimension</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChartDimension get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChartDimension result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Chart Dimension</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChartDimension getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChartDimension result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Chart Dimension</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChartDimension get(int value) {
		switch (value) {
		case TWO_DIMENSIONAL:
			return TWO_DIMENSIONAL_LITERAL;
		case TWO_DIMENSIONAL_WITH_DEPTH:
			return TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
		case THREE_DIMENSIONAL:
			return THREE_DIMENSIONAL_LITERAL;
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
	private ChartDimension(int value, String name, String literal) {
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
