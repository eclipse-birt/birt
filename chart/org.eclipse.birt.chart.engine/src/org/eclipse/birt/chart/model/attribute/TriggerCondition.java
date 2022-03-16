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
 * <em><b>Trigger Condition</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 *
 * This type defines the allowed values for Trigger conditions (for
 * interactivity with chart). Any new condition needs to be added here to be
 * supported.
 *
 * <!-- end-model-doc -->
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getTriggerCondition()
 * @model
 * @generated
 */
public enum TriggerCondition implements Enumerator {
	/**
	 * The '<em><b>Mouse Hover</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Mouse Hover</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MOUSE_HOVER
	 * @generated
	 * @ordered
	 * @deprecated use ONMOUSEOVER_LITERAL instead.
	 */
	MOUSE_HOVER_LITERAL(0, "MouseHover", "Mouse_Hover"),
	/**
	 * The '<em><b>Mouse Click</b></em>' literal object. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Mouse Click</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #MOUSE_CLICK
	 * @generated
	 * @ordered
	 * @deprecated use ONCLICK_LITERAL instead.
	 */
	MOUSE_CLICK_LITERAL(1, "MouseClick", "Mouse_Click"),
	/**
	 * The '<em><b>Onclick</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONCLICK
	 * @generated
	 * @ordered
	 */
	ONCLICK_LITERAL(2, "onclick", "onclick"),
	/**
	 * The '<em><b>Ondblclick</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONDBLCLICK
	 * @generated
	 * @ordered
	 */
	ONDBLCLICK_LITERAL(3, "ondblclick", "ondblclick"),
	/**
	 * The '<em><b>Onmousedown</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEDOWN
	 * @generated
	 * @ordered
	 */
	ONMOUSEDOWN_LITERAL(4, "onmousedown", "onmousedown"),
	/**
	 * The '<em><b>Onmouseup</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONMOUSEUP
	 * @generated
	 * @ordered
	 */
	ONMOUSEUP_LITERAL(5, "onmouseup", "onmouseup"),
	/**
	 * The '<em><b>Onmouseover</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEOVER
	 * @generated
	 * @ordered
	 */
	ONMOUSEOVER_LITERAL(6, "onmouseover", "onmouseover"),
	/**
	 * The '<em><b>Onmousemove</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEMOVE
	 * @generated
	 * @ordered
	 */
	ONMOUSEMOVE_LITERAL(7, "onmousemove", "onmousemove"),
	/**
	 * The '<em><b>Onmouseout</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONMOUSEOUT
	 * @generated
	 * @ordered
	 */
	ONMOUSEOUT_LITERAL(8, "onmouseout", "onmouseout"),
	/**
	 * The '<em><b>Onfocus</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONFOCUS
	 * @generated
	 * @ordered
	 */
	ONFOCUS_LITERAL(9, "onfocus", "onfocus"),
	/**
	 * The '<em><b>Onblur</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONBLUR
	 * @generated
	 * @ordered
	 */
	ONBLUR_LITERAL(10, "onblur", "onblur"),
	/**
	 * The '<em><b>Onkeydown</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONKEYDOWN
	 * @generated
	 * @ordered
	 */
	ONKEYDOWN_LITERAL(11, "onkeydown", "onkeydown"),
	/**
	 * The '<em><b>Onkeypress</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONKEYPRESS
	 * @generated
	 * @ordered
	 */
	ONKEYPRESS_LITERAL(12, "onkeypress", "onkeypress"),
	/**
	 * The '<em><b>Onkeyup</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONKEYUP
	 * @generated
	 * @ordered
	 */
	ONKEYUP_LITERAL(13, "onkeyup", "onkeyup"),
	/**
	 * The '<em><b>Accessibility</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ACCESSIBILITY
	 * @generated
	 * @ordered
	 */
	ACCESSIBILITY_LITERAL(14, "accessibility", "accessibility"),
	/**
	 * The '<em><b>Onload</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @see #ONLOAD
	 * @generated
	 * @ordered
	 */
	ONLOAD_LITERAL(15, "onload", "onload"),
	/**
	 * The '<em><b>Onrightclick</b></em>' literal object. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ONRIGHTCLICK
	 * @generated
	 * @ordered
	 */
	ONRIGHTCLICK_LITERAL(16, "onrightclick", "onrightclick");

	/**
	 * The '<em><b>Mouse Hover</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> deprecated <!-- end-model-doc -->
	 *
	 * @see #MOUSE_HOVER_LITERAL
	 * @model name="MouseHover" literal="Mouse_Hover"
	 * @generated
	 * @ordered
	 */
	public static final int MOUSE_HOVER = 0;

	/**
	 * The '<em><b>Mouse Click</b></em>' literal value. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> deprecated <!-- end-model-doc -->
	 *
	 * @see #MOUSE_CLICK_LITERAL
	 * @model name="MouseClick" literal="Mouse_Click"
	 * @generated
	 * @ordered
	 */
	public static final int MOUSE_CLICK = 1;

	/**
	 * The '<em><b>Onclick</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onclick</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONCLICK_LITERAL
	 * @model name="onclick"
	 * @generated
	 * @ordered
	 */
	public static final int ONCLICK = 2;

	/**
	 * The '<em><b>Ondblclick</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Ondblclick</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONDBLCLICK_LITERAL
	 * @model name="ondblclick"
	 * @generated
	 * @ordered
	 */
	public static final int ONDBLCLICK = 3;

	/**
	 * The '<em><b>Onmousedown</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onmousedown</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEDOWN_LITERAL
	 * @model name="onmousedown"
	 * @generated
	 * @ordered
	 */
	public static final int ONMOUSEDOWN = 4;

	/**
	 * The '<em><b>Onmouseup</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onmouseup</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEUP_LITERAL
	 * @model name="onmouseup"
	 * @generated
	 * @ordered
	 */
	public static final int ONMOUSEUP = 5;

	/**
	 * The '<em><b>Onmouseover</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onmouseover</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEOVER_LITERAL
	 * @model name="onmouseover"
	 * @generated
	 * @ordered
	 */
	public static final int ONMOUSEOVER = 6;

	/**
	 * The '<em><b>Onmousemove</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onmousemove</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEMOVE_LITERAL
	 * @model name="onmousemove"
	 * @generated
	 * @ordered
	 */
	public static final int ONMOUSEMOVE = 7;

	/**
	 * The '<em><b>Onmouseout</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onmouseout</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONMOUSEOUT_LITERAL
	 * @model name="onmouseout"
	 * @generated
	 * @ordered
	 */
	public static final int ONMOUSEOUT = 8;

	/**
	 * The '<em><b>Onfocus</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onfocus</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONFOCUS_LITERAL
	 * @model name="onfocus"
	 * @generated
	 * @ordered
	 */
	public static final int ONFOCUS = 9;

	/**
	 * The '<em><b>Onblur</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onblur</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONBLUR_LITERAL
	 * @model name="onblur"
	 * @generated
	 * @ordered
	 */
	public static final int ONBLUR = 10;

	/**
	 * The '<em><b>Onkeydown</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onkeydown</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONKEYDOWN_LITERAL
	 * @model name="onkeydown"
	 * @generated
	 * @ordered
	 */
	public static final int ONKEYDOWN = 11;

	/**
	 * The '<em><b>Onkeypress</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onkeypress</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONKEYPRESS_LITERAL
	 * @model name="onkeypress"
	 * @generated
	 * @ordered
	 */
	public static final int ONKEYPRESS = 12;

	/**
	 * The '<em><b>Onkeyup</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onkeyup</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONKEYUP_LITERAL
	 * @model name="onkeyup"
	 * @generated
	 * @ordered
	 */
	public static final int ONKEYUP = 13;

	/**
	 * The '<em><b>Accessibility</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Accessibility</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ACCESSIBILITY_LITERAL
	 * @model name="accessibility"
	 * @generated
	 * @ordered
	 */
	public static final int ACCESSIBILITY = 14;

	/**
	 * The '<em><b>Onload</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onload</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONLOAD_LITERAL
	 * @model name="onload"
	 * @generated
	 * @ordered
	 */
	public static final int ONLOAD = 15;

	/**
	 * The '<em><b>Onrightclick</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Onrightclick</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @see #ONRIGHTCLICK_LITERAL
	 * @model name="onrightclick"
	 * @generated
	 * @ordered
	 */
	public static final int ONRIGHTCLICK = 16;

	/**
	 * An array of all the '<em><b>Trigger Condition</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final TriggerCondition[] VALUES_ARRAY = { MOUSE_HOVER_LITERAL, MOUSE_CLICK_LITERAL, ONCLICK_LITERAL,
			ONDBLCLICK_LITERAL, ONMOUSEDOWN_LITERAL, ONMOUSEUP_LITERAL, ONMOUSEOVER_LITERAL, ONMOUSEMOVE_LITERAL,
			ONMOUSEOUT_LITERAL, ONFOCUS_LITERAL, ONBLUR_LITERAL, ONKEYDOWN_LITERAL, ONKEYPRESS_LITERAL, ONKEYUP_LITERAL,
			ACCESSIBILITY_LITERAL, ONLOAD_LITERAL, ONRIGHTCLICK_LITERAL, };

	/**
	 * A public read-only list of all the '<em><b>Trigger Condition</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<TriggerCondition> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Trigger Condition</b></em>' literal with the specified
	 * literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static TriggerCondition get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TriggerCondition result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Trigger Condition</b></em>' literal with the specified
	 * name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static TriggerCondition getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			TriggerCondition result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Trigger Condition</b></em>' literal with the specified
	 * integer value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static TriggerCondition get(int value) {
		switch (value) {
		case MOUSE_HOVER:
			return MOUSE_HOVER_LITERAL;
		case MOUSE_CLICK:
			return MOUSE_CLICK_LITERAL;
		case ONCLICK:
			return ONCLICK_LITERAL;
		case ONDBLCLICK:
			return ONDBLCLICK_LITERAL;
		case ONMOUSEDOWN:
			return ONMOUSEDOWN_LITERAL;
		case ONMOUSEUP:
			return ONMOUSEUP_LITERAL;
		case ONMOUSEOVER:
			return ONMOUSEOVER_LITERAL;
		case ONMOUSEMOVE:
			return ONMOUSEMOVE_LITERAL;
		case ONMOUSEOUT:
			return ONMOUSEOUT_LITERAL;
		case ONFOCUS:
			return ONFOCUS_LITERAL;
		case ONBLUR:
			return ONBLUR_LITERAL;
		case ONKEYDOWN:
			return ONKEYDOWN_LITERAL;
		case ONKEYPRESS:
			return ONKEYPRESS_LITERAL;
		case ONKEYUP:
			return ONKEYUP_LITERAL;
		case ACCESSIBILITY:
			return ACCESSIBILITY_LITERAL;
		case ONLOAD:
			return ONLOAD_LITERAL;
		case ONRIGHTCLICK:
			return ONRIGHTCLICK_LITERAL;
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
	TriggerCondition(int value, String name, String literal) {
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
