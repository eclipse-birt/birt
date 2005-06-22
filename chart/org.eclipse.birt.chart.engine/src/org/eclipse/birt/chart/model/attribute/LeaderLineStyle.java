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
public final class LeaderLineStyle extends AbstractEnumerator
{

	/**
	 * The '<em><b>Fixed Length</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #FIXED_LENGTH_LITERAL
	 * @model name="Fixed_Length"
	 * @generated
	 * @ordered
	 */
	public static final int FIXED_LENGTH = 0;

	/**
	 * The '<em><b>Stretch To Side</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #STRETCH_TO_SIDE_LITERAL
	 * @model name="Stretch_To_Side"
	 * @generated
	 * @ordered
	 */
	public static final int STRETCH_TO_SIDE = 1;

	/**
	 * The '<em><b>Fixed Length</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Fixed Length</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #FIXED_LENGTH
	 * @generated
	 * @ordered
	 */
	public static final LeaderLineStyle FIXED_LENGTH_LITERAL = new LeaderLineStyle( FIXED_LENGTH,
			"Fixed_Length" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Stretch To Side</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Stretch To Side</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #STRETCH_TO_SIDE
	 * @generated
	 * @ordered
	 */
	public static final LeaderLineStyle STRETCH_TO_SIDE_LITERAL = new LeaderLineStyle( STRETCH_TO_SIDE,
			"Stretch_To_Side" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Leader Line Style</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final LeaderLineStyle[] VALUES_ARRAY = new LeaderLineStyle[]{
			FIXED_LENGTH_LITERAL, STRETCH_TO_SIDE_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Leader Line Style</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Leader Line Style</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LeaderLineStyle get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			LeaderLineStyle result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Leader Line Style</b></em>' literal with the
	 * specified value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static LeaderLineStyle get( int value )
	{
		switch ( value )
		{
			case FIXED_LENGTH :
				return FIXED_LENGTH_LITERAL;
			case STRETCH_TO_SIDE :
				return STRETCH_TO_SIDE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private LeaderLineStyle( int value, String name )
	{
		super( value, name );
	}

} //LeaderLineStyle
