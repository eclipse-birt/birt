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
 * <em><b>Anchor</b></em>', and utility methods for working with them. <!--
 * end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the options available for anchoring blocks.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getAnchor()
 * @model
 * @generated
 */
public final class Anchor extends AbstractEnumerator
{

	/**
	 * The '<em><b>North</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NORTH_LITERAL
	 * @model name="North"
	 * @generated
	 * @ordered
	 */
	public static final int NORTH = 0;

	/**
	 * The '<em><b>North East</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #NORTH_EAST_LITERAL
	 * @model name="North_East"
	 * @generated
	 * @ordered
	 */
	public static final int NORTH_EAST = 1;

	/**
	 * The '<em><b>East</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EAST_LITERAL
	 * @model name="East"
	 * @generated
	 * @ordered
	 */
	public static final int EAST = 2;

	/**
	 * The '<em><b>South East</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #SOUTH_EAST_LITERAL
	 * @model name="South_East"
	 * @generated
	 * @ordered
	 */
	public static final int SOUTH_EAST = 3;

	/**
	 * The '<em><b>South</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOUTH_LITERAL
	 * @model name="South"
	 * @generated
	 * @ordered
	 */
	public static final int SOUTH = 4;

	/**
	 * The '<em><b>South West</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #SOUTH_WEST_LITERAL
	 * @model name="South_West"
	 * @generated
	 * @ordered
	 */
	public static final int SOUTH_WEST = 5;

	/**
	 * The '<em><b>West</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WEST_LITERAL
	 * @model name="West"
	 * @generated
	 * @ordered
	 */
	public static final int WEST = 6;

	/**
	 * The '<em><b>North West</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #NORTH_WEST_LITERAL
	 * @model name="North_West"
	 * @generated
	 * @ordered
	 */
	public static final int NORTH_WEST = 7;

	/**
	 * The '<em><b>North</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>North</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NORTH
	 * @generated
	 * @ordered
	 */
	public static final Anchor NORTH_LITERAL = new Anchor( NORTH, "North" ); //$NON-NLS-1$

	/**
	 * The '<em><b>North East</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>North East</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NORTH_EAST
	 * @generated
	 * @ordered
	 */
	public static final Anchor NORTH_EAST_LITERAL = new Anchor( NORTH_EAST,
			"North_East" ); //$NON-NLS-1$

	/**
	 * The '<em><b>East</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>East</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EAST
	 * @generated
	 * @ordered
	 */
	public static final Anchor EAST_LITERAL = new Anchor( EAST, "East" ); //$NON-NLS-1$

	/**
	 * The '<em><b>South East</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>South East</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOUTH_EAST
	 * @generated
	 * @ordered
	 */
	public static final Anchor SOUTH_EAST_LITERAL = new Anchor( SOUTH_EAST,
			"South_East" ); //$NON-NLS-1$

	/**
	 * The '<em><b>South</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>South</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOUTH
	 * @generated
	 * @ordered
	 */
	public static final Anchor SOUTH_LITERAL = new Anchor( SOUTH, "South" ); //$NON-NLS-1$

	/**
	 * The '<em><b>South West</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>South West</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOUTH_WEST
	 * @generated
	 * @ordered
	 */
	public static final Anchor SOUTH_WEST_LITERAL = new Anchor( SOUTH_WEST,
			"South_West" ); //$NON-NLS-1$

	/**
	 * The '<em><b>West</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>West</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WEST
	 * @generated
	 * @ordered
	 */
	public static final Anchor WEST_LITERAL = new Anchor( WEST, "West" ); //$NON-NLS-1$

	/**
	 * The '<em><b>North West</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>North West</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NORTH_WEST
	 * @generated
	 * @ordered
	 */
	public static final Anchor NORTH_WEST_LITERAL = new Anchor( NORTH_WEST,
			"North_West" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Anchor</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final Anchor[] VALUES_ARRAY = new Anchor[]{
			NORTH_LITERAL,
			NORTH_EAST_LITERAL,
			EAST_LITERAL,
			SOUTH_EAST_LITERAL,
			SOUTH_LITERAL,
			SOUTH_WEST_LITERAL,
			WEST_LITERAL,
			NORTH_WEST_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Anchor</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Anchor</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static Anchor get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			Anchor result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Anchor</b></em>' literal with the specified value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static Anchor get( int value )
	{
		switch ( value )
		{
			case NORTH :
				return NORTH_LITERAL;
			case NORTH_EAST :
				return NORTH_EAST_LITERAL;
			case EAST :
				return EAST_LITERAL;
			case SOUTH_EAST :
				return SOUTH_EAST_LITERAL;
			case SOUTH :
				return SOUTH_LITERAL;
			case SOUTH_WEST :
				return SOUTH_WEST_LITERAL;
			case WEST :
				return WEST_LITERAL;
			case NORTH_WEST :
				return NORTH_WEST_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	private Anchor( int value, String name )
	{
		super( value, name );
	}

} //Anchor
