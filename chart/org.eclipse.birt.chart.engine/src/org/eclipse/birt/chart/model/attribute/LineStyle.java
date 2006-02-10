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
 * <em><b>Line Style</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for line styles.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getLineStyle()
 * @model
 * @generated
 */
public final class LineStyle extends AbstractEnumerator
{

	/**
	 * The '<em><b>Solid</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOLID_LITERAL
	 * @model name="Solid"
	 * @generated
	 * @ordered
	 */
	public static final int SOLID = 0;

	/**
	 * The '<em><b>Dashed</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DASHED_LITERAL
	 * @model name="Dashed"
	 * @generated
	 * @ordered
	 */
	public static final int DASHED = 1;

	/**
	 * The '<em><b>Dotted</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DOTTED_LITERAL
	 * @model name="Dotted"
	 * @generated
	 * @ordered
	 */
	public static final int DOTTED = 2;

	/**
	 * The '<em><b>Dash Dotted</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #DASH_DOTTED_LITERAL
	 * @model name="DashDotted" literal="Dash_Dotted"
	 * @generated
	 * @ordered
	 */
	public static final int DASH_DOTTED = 3;

	/**
	 * The '<em><b>Solid</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Solid</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOLID
	 * @generated
	 * @ordered
	 */
	public static final LineStyle SOLID_LITERAL = new LineStyle( SOLID,
			"Solid", "Solid" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Dashed</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Dashed</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DASHED
	 * @generated
	 * @ordered
	 */
	public static final LineStyle DASHED_LITERAL = new LineStyle( DASHED,
			"Dashed", "Dashed" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Dotted</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Dotted</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DOTTED
	 * @generated
	 * @ordered
	 */
	public static final LineStyle DOTTED_LITERAL = new LineStyle( DOTTED,
			"Dotted", "Dotted" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Dash Dotted</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Dash Dotted</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #DASH_DOTTED
	 * @generated
	 * @ordered
	 */
	public static final LineStyle DASH_DOTTED_LITERAL = new LineStyle( DASH_DOTTED,
			"DashDotted", "Dash_Dotted" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Line Style</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final LineStyle[] VALUES_ARRAY = new LineStyle[]{
			SOLID_LITERAL, DASHED_LITERAL, DOTTED_LITERAL, DASH_DOTTED_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Line Style</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Line Style</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static LineStyle get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			LineStyle result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Line Style</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LineStyle getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			LineStyle result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Line Style</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static LineStyle get( int value )
	{
		switch ( value )
		{
			case SOLID :
				return SOLID_LITERAL;
			case DASHED :
				return DASHED_LITERAL;
			case DOTTED :
				return DOTTED_LITERAL;
			case DASH_DOTTED :
				return DASH_DOTTED_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private LineStyle( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //LineStyle
