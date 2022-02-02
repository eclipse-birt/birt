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
 * $Id: CursorType.java,v 1.1 2009/01/04 10:05:02 heli Exp $
 */

package org.eclipse.birt.chart.model.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration
 * '<em><b>Cursor Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc --> The cursor type defines
 * standard cursor types which are supported by all devices, including SWT,
 * Swing, SVG and HTML/Image. <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCursorType()
 * @model extendedMetaData="name='CursorType'"
 * @generated
 */
public enum CursorType implements Enumerator {
	/**
	 * The '<em><b>Auto</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #AUTO_VALUE
	 * @generated
	 * @ordered
	 */
	AUTO(0, "Auto", "Auto"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Crosshair</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CROSSHAIR_VALUE
	 * @generated
	 * @ordered
	 */
	CROSSHAIR(1, "Crosshair", "Crosshair"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Default</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #DEFAULT_VALUE
	 * @generated
	 * @ordered
	 */
	DEFAULT(2, "Default", "Default"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Pointer</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #POINTER_VALUE
	 * @generated
	 * @ordered
	 */
	POINTER(3, "Pointer", "Pointer"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Move</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #MOVE_VALUE
	 * @generated
	 * @ordered
	 */
	MOVE(4, "Move", "Move"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Text</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #TEXT_VALUE
	 * @generated
	 * @ordered
	 */
	TEXT(5, "Text", "Text"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Wait</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #WAIT_VALUE
	 * @generated
	 * @ordered
	 */
	WAIT(6, "Wait", "Wait"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>EResize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #ERESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	ERESIZE(7, "EResize", "E-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>NE Resize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #NE_RESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	NE_RESIZE(8, "NEResize", "NE-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>NW Resize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #NW_RESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	NW_RESIZE(9, "NWResize", "NW-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>NResize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #NRESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	NRESIZE(10, "NResize", "N-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>SE Resize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #SE_RESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	SE_RESIZE(11, "SEResize", "SE-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>SW Resize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #SW_RESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	SW_RESIZE(12, "SWResize", "SW-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>SResize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #SRESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	SRESIZE(13, "SResize", "S-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>WResize</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #WRESIZE_VALUE
	 * @generated
	 * @ordered
	 */
	WRESIZE(14, "WResize", "W-Resize"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Custom</b></em>' literal object. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #CUSTOM_VALUE
	 * @generated
	 * @ordered
	 */
	CUSTOM(15, "Custom", "Custom"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Auto</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Auto</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #AUTO
	 * @model name="Auto"
	 * @generated
	 * @ordered
	 */
	public static final int AUTO_VALUE = 0;

	/**
	 * The '<em><b>Crosshair</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Crosshair</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CROSSHAIR
	 * @model name="Crosshair"
	 * @generated
	 * @ordered
	 */
	public static final int CROSSHAIR_VALUE = 1;

	/**
	 * The '<em><b>Default</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Default</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #DEFAULT
	 * @model name="Default"
	 * @generated
	 * @ordered
	 */
	public static final int DEFAULT_VALUE = 2;

	/**
	 * The '<em><b>Pointer</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Pointer</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #POINTER
	 * @model name="Pointer"
	 * @generated
	 * @ordered
	 */
	public static final int POINTER_VALUE = 3;

	/**
	 * The '<em><b>Move</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Move</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #MOVE
	 * @model name="Move"
	 * @generated
	 * @ordered
	 */
	public static final int MOVE_VALUE = 4;

	/**
	 * The '<em><b>Text</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Text</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TEXT
	 * @model name="Text"
	 * @generated
	 * @ordered
	 */
	public static final int TEXT_VALUE = 5;

	/**
	 * The '<em><b>Wait</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Wait</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #WAIT
	 * @model name="Wait"
	 * @generated
	 * @ordered
	 */
	public static final int WAIT_VALUE = 6;

	/**
	 * The '<em><b>EResize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EResize</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ERESIZE
	 * @model name="EResize" literal="E-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int ERESIZE_VALUE = 7;

	/**
	 * The '<em><b>NE Resize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NE Resize</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NE_RESIZE
	 * @model name="NEResize" literal="NE-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int NE_RESIZE_VALUE = 8;

	/**
	 * The '<em><b>NW Resize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NW Resize</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NW_RESIZE
	 * @model name="NWResize" literal="NW-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int NW_RESIZE_VALUE = 9;

	/**
	 * The '<em><b>NResize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NResize</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #NRESIZE
	 * @model name="NResize" literal="N-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int NRESIZE_VALUE = 10;

	/**
	 * The '<em><b>SE Resize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SE Resize</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SE_RESIZE
	 * @model name="SEResize" literal="SE-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int SE_RESIZE_VALUE = 11;

	/**
	 * The '<em><b>SW Resize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SW Resize</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SW_RESIZE
	 * @model name="SWResize" literal="SW-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int SW_RESIZE_VALUE = 12;

	/**
	 * The '<em><b>SResize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SResize</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SRESIZE
	 * @model name="SResize" literal="S-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int SRESIZE_VALUE = 13;

	/**
	 * The '<em><b>WResize</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WResize</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #WRESIZE
	 * @model name="WResize" literal="W-Resize"
	 * @generated
	 * @ordered
	 */
	public static final int WRESIZE_VALUE = 14;

	/**
	 * The '<em><b>Custom</b></em>' literal value. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Custom</b></em>' literal object isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #CUSTOM
	 * @model name="Custom"
	 * @generated
	 * @ordered
	 */
	public static final int CUSTOM_VALUE = 15;

	/**
	 * An array of all the '<em><b>Cursor Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final CursorType[] VALUES_ARRAY = new CursorType[] { AUTO, CROSSHAIR, DEFAULT, POINTER, MOVE, TEXT,
			WAIT, ERESIZE, NE_RESIZE, NW_RESIZE, NRESIZE, SE_RESIZE, SW_RESIZE, SRESIZE, WRESIZE, CUSTOM, };

	/**
	 * A public read-only list of all the '<em><b>Cursor Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<CursorType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Cursor Type</b></em>' literal with the specified literal
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static CursorType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CursorType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Cursor Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static CursorType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			CursorType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Cursor Type</b></em>' literal with the specified integer
	 * value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static CursorType get(int value) {
		switch (value) {
		case AUTO_VALUE:
			return AUTO;
		case CROSSHAIR_VALUE:
			return CROSSHAIR;
		case DEFAULT_VALUE:
			return DEFAULT;
		case POINTER_VALUE:
			return POINTER;
		case MOVE_VALUE:
			return MOVE;
		case TEXT_VALUE:
			return TEXT;
		case WAIT_VALUE:
			return WAIT;
		case ERESIZE_VALUE:
			return ERESIZE;
		case NE_RESIZE_VALUE:
			return NE_RESIZE;
		case NW_RESIZE_VALUE:
			return NW_RESIZE;
		case NRESIZE_VALUE:
			return NRESIZE;
		case SE_RESIZE_VALUE:
			return SE_RESIZE;
		case SW_RESIZE_VALUE:
			return SW_RESIZE;
		case SRESIZE_VALUE:
			return SRESIZE;
		case WRESIZE_VALUE:
			return WRESIZE;
		case CUSTOM_VALUE:
			return CUSTOM;
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
	private CursorType(int value, String name, String literal) {
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

} // CursorType
