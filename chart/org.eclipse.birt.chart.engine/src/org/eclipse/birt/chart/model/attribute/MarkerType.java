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
 * <em><b>Marker Type</b></em>', and utility methods for working with them.
 * <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type represents the possible values for markers supported for Line
 * Series.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getMarkerType()
 * @model
 * @generated
 */
public final class MarkerType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Crosshair</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #CROSSHAIR_LITERAL
	 * @model name="Crosshair"
	 * @generated
	 * @ordered
	 */
	public static final int CROSSHAIR = 0;

	/**
	 * The '<em><b>Triangle</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #TRIANGLE_LITERAL
	 * @model name="Triangle"
	 * @generated
	 * @ordered
	 */
	public static final int TRIANGLE = 1;

	/**
	 * The '<em><b>Box</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BOX_LITERAL
	 * @model name="Box"
	 * @generated
	 * @ordered
	 */
	public static final int BOX = 2;

	/**
	 * The '<em><b>Circle</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CIRCLE_LITERAL
	 * @model name="Circle"
	 * @generated
	 * @ordered
	 */
	public static final int CIRCLE = 3;

	/**
	 * The '<em><b>Icon</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Icon</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ICON_LITERAL
	 * @model name="Icon"
	 * @generated
	 * @ordered
	 */
	public static final int ICON = 4;

	/**
	 * The '<em><b>Crosshair</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Crosshair</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CROSSHAIR
	 * @generated
	 * @ordered
	 */
	public static final MarkerType CROSSHAIR_LITERAL = new MarkerType( CROSSHAIR,
			"Crosshair" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Triangle</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Triangle</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TRIANGLE
	 * @generated
	 * @ordered
	 */
	public static final MarkerType TRIANGLE_LITERAL = new MarkerType( TRIANGLE,
			"Triangle" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Box</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Box</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BOX
	 * @generated
	 * @ordered
	 */
	public static final MarkerType BOX_LITERAL = new MarkerType( BOX, "Box" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Circle</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Circle</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CIRCLE
	 * @generated
	 * @ordered
	 */
	public static final MarkerType CIRCLE_LITERAL = new MarkerType( CIRCLE,
			"Circle" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Icon</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ICON
	 * @generated
	 * @ordered
	 */
	public static final MarkerType ICON_LITERAL = new MarkerType( ICON, "Icon" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Marker Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final MarkerType[] VALUES_ARRAY = new MarkerType[]{
			CROSSHAIR_LITERAL,
			TRIANGLE_LITERAL,
			BOX_LITERAL,
			CIRCLE_LITERAL,
			ICON_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Marker Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static MarkerType get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			MarkerType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static MarkerType get( int value )
	{
		switch ( value )
		{
			case CROSSHAIR :
				return CROSSHAIR_LITERAL;
			case TRIANGLE :
				return TRIANGLE_LITERAL;
			case BOX :
				return BOX_LITERAL;
			case CIRCLE :
				return CIRCLE_LITERAL;
			case ICON :
				return ICON_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	private MarkerType( int value, String name )
	{
		super( value, name );
	}

} //MarkerType
