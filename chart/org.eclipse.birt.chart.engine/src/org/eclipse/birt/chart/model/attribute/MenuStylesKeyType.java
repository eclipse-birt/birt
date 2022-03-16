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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration
 * '<em><b>Menu Styles Key Type</b></em>', and utility methods for working with
 * them. <!-- end-user-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMenuStylesKeyType()
 * @model extendedMetaData="name='MenuStylesKeyType'"
 * @generated
 */
public enum MenuStylesKeyType implements Enumerator {
	/**
	 * The '<em><b>Menu</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #MENU_VALUE
	 * @generated
	 * @ordered
	 */
	MENU(0, "Menu", "Menu"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Menu Item</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #MENU_ITEM_VALUE
	 * @generated
	 * @ordered
	 */
	MENU_ITEM(1, "MenuItem", "MenuItem"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>On Mouse Over</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ON_MOUSE_OVER_VALUE
	 * @generated
	 * @ordered
	 */
	ON_MOUSE_OVER(2, "OnMouseOver", "OnMouseOver"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>On Mouse Out</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ON_MOUSE_OUT_VALUE
	 * @generated
	 * @ordered
	 */
	ON_MOUSE_OUT(3, "OnMouseOut", "OnMouseOut"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Menu</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Menu</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MENU
	 * @model name="Menu"
	 * @generated
	 * @ordered
	 */
	public static final int MENU_VALUE = 0;

	/**
	 * The '<em><b>Menu Item</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Menu Item</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MENU_ITEM
	 * @model name="MenuItem"
	 * @generated
	 * @ordered
	 */
	public static final int MENU_ITEM_VALUE = 1;

	/**
	 * The '<em><b>On Mouse Over</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>On Mouse Over</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ON_MOUSE_OVER
	 * @model name="OnMouseOver"
	 * @generated
	 * @ordered
	 */
	public static final int ON_MOUSE_OVER_VALUE = 2;

	/**
	 * The '<em><b>On Mouse Out</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>On Mouse Out</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ON_MOUSE_OUT
	 * @model name="OnMouseOut"
	 * @generated
	 * @ordered
	 */
	public static final int ON_MOUSE_OUT_VALUE = 3;

	/**
	 * An array of all the '<em><b>Menu Styles Key Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final MenuStylesKeyType[] VALUES_ARRAY = { MENU, MENU_ITEM, ON_MOUSE_OVER, ON_MOUSE_OUT, };

	/**
	 * A public read-only list of all the '<em><b>Menu Styles Key Type</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<MenuStylesKeyType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Menu Styles Key Type</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static MenuStylesKeyType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MenuStylesKeyType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Menu Styles Key Type</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static MenuStylesKeyType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MenuStylesKeyType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Menu Styles Key Type</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static MenuStylesKeyType get(int value) {
		switch (value) {
		case MENU_VALUE:
			return MENU;
		case MENU_ITEM_VALUE:
			return MENU_ITEM;
		case ON_MOUSE_OVER_VALUE:
			return ON_MOUSE_OVER;
		case ON_MOUSE_OUT_VALUE:
			return ON_MOUSE_OUT;
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
	MenuStylesKeyType(int value, String name, String literal) {
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

} // MenuStylesKeyType
