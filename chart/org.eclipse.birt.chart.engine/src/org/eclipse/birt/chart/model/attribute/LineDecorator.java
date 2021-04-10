/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration
 * '<em><b>Line Decorator</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible line head decorators. <!-- end-model-doc
 * -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineDecorator()
 * @model
 * @generated
 */
public enum LineDecorator implements Enumerator {
	/**
	 * The '<em><b>Arrow</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #ARROW
	 * @generated
	 * @ordered
	 */
	ARROW_LITERAL(0, "Arrow", "Arrow"),
	/**
	 * The '<em><b>Circle</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CIRCLE
	 * @generated
	 * @ordered
	 */
	CIRCLE_LITERAL(1, "Circle", "Circle"),
	/**
	 * The '<em><b>None</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
	NONE_LITERAL(2, "None", "None");

	/**
	 * The '<em><b>Arrow</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Arrow</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ARROW_LITERAL
	 * @model name="Arrow"
	 * @generated
	 * @ordered
	 */
	public static final int ARROW = 0;

	/**
	 * The '<em><b>Circle</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Circle</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CIRCLE_LITERAL
	 * @model name="Circle"
	 * @generated
	 * @ordered
	 */
	public static final int CIRCLE = 1;

	/**
	 * The '<em><b>None</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>None</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NONE_LITERAL
	 * @model name="None"
	 * @generated
	 * @ordered
	 */
	public static final int NONE = 2;

	/**
	 * An array of all the '<em><b>Line Decorator</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final LineDecorator[] VALUES_ARRAY = new LineDecorator[] { ARROW_LITERAL, CIRCLE_LITERAL,
			NONE_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Line Decorator</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<LineDecorator> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Line Decorator</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LineDecorator get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LineDecorator result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Line Decorator</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LineDecorator getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LineDecorator result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Line Decorator</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LineDecorator get(int value) {
		switch (value) {
		case ARROW:
			return ARROW_LITERAL;
		case CIRCLE:
			return CIRCLE_LITERAL;
		case NONE:
			return NONE_LITERAL;
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
	private LineDecorator(int value, String name, String literal) {
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
