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

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '
 * <em><b>Axis Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for Axis types. Any new Axis type needs
 * to be added here to be supported.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAxisType()
 * @model
 * @generated
 */
public final class AxisType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Linear</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LINEAR_LITERAL
	 * @model name="Linear"
	 * @generated
	 * @ordered
	 */
	public static final int LINEAR = 0;

	/**
	 * The '<em><b>Logarithmic</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #LOGARITHMIC_LITERAL
	 * @model name="Logarithmic"
	 * @generated
	 * @ordered
	 */
	public static final int LOGARITHMIC = 1;

	/**
	 * The '<em><b>Text</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TEXT_LITERAL
	 * @model name="Text"
	 * @generated
	 * @ordered
	 */
	public static final int TEXT = 2;

	/**
	 * The '<em><b>Date Time</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #DATE_TIME_LITERAL
	 * @model name="DateTime"
	 * @generated
	 * @ordered
	 */
	public static final int DATE_TIME = 3;

	/**
	 * The '<em><b>Linear</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Linear</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LINEAR
	 * @generated
	 * @ordered
	 */
	public static final AxisType LINEAR_LITERAL = new AxisType( LINEAR,
			"Linear" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Logarithmic</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Logarithmic</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #LOGARITHMIC
	 * @generated
	 * @ordered
	 */
	public static final AxisType LOGARITHMIC_LITERAL = new AxisType( LOGARITHMIC,
			"Logarithmic" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Text</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Text</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TEXT
	 * @generated
	 * @ordered
	 */
	public static final AxisType TEXT_LITERAL = new AxisType( TEXT, "Text" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Date Time</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Date Time</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DATE_TIME
	 * @generated
	 * @ordered
	 */
	public static final AxisType DATE_TIME_LITERAL = new AxisType( DATE_TIME,
			"DateTime" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Axis Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final AxisType[] VALUES_ARRAY = new AxisType[]{
			LINEAR_LITERAL,
			LOGARITHMIC_LITERAL,
			TEXT_LITERAL,
			DATE_TIME_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Axis Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Axis Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static AxisType get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			AxisType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Axis Type</b></em>' literal with the specified value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static AxisType get( int value )
	{
		switch ( value )
		{
			case LINEAR :
				return LINEAR_LITERAL;
			case LOGARITHMIC :
				return LOGARITHMIC_LITERAL;
			case TEXT :
				return TEXT_LITERAL;
			case DATE_TIME :
				return DATE_TIME_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	private AxisType( int value, String name )
	{
		super( value, name );
	}

} //AxisType
