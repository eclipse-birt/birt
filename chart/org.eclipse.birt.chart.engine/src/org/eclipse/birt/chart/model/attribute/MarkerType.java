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
	 * The '<em><b>Nabla</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Nabla</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NABLA_LITERAL
	 * @model name="Nabla"
	 * @generated
	 * @ordered
	 */
	public static final int NABLA = 5;

	/**
	 * The '<em><b>Diamond</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Diamond</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DIAMOND_LITERAL
	 * @model name="Diamond"
	 * @generated
	 * @ordered
	 */
	public static final int DIAMOND = 6;

	/**
	 * The '<em><b>Four Diamonds</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Four Diamonds</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FOUR_DIAMONDS_LITERAL
	 * @model name="FourDiamonds" literal="Four_Diamonds"
	 * @generated
	 * @ordered
	 */
	public static final int FOUR_DIAMONDS = 7;

	/**
	 * The '<em><b>Button</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Button</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BUTTON_LITERAL
	 * @model name="Button"
	 * @generated
	 * @ordered
	 */
	public static final int BUTTON = 8;

	/**
	 * The '<em><b>Semi Circle</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Semi Circle</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SEMI_CIRCLE_LITERAL
	 * @model name="SemiCircle" literal="Semi_Circle"
	 * @generated
	 * @ordered
	 */
	public static final int SEMI_CIRCLE = 9;

	/**
	 * The '<em><b>Hexagon</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Hexagon</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #HEXAGON_LITERAL
	 * @model name="Hexagon"
	 * @generated
	 * @ordered
	 */
	public static final int HEXAGON = 10;

	/**
	 * The '<em><b>Rectangle</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Rectangle</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #RECTANGLE_LITERAL
	 * @model name="Rectangle"
	 * @generated
	 * @ordered
	 */
	public static final int RECTANGLE = 11;

	/**
	 * The '<em><b>Star</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Star</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STAR_LITERAL
	 * @model name="Star"
	 * @generated
	 * @ordered
	 */
	public static final int STAR = 12;

	/**
	 * The '<em><b>Column</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Column</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #COLUMN_LITERAL
	 * @model name="Column"
	 * @generated
	 * @ordered
	 */
	public static final int COLUMN = 13;

	/**
	 * The '<em><b>Cross</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Cross</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CROSS_LITERAL
	 * @model name="Cross"
	 * @generated
	 * @ordered
	 */
	public static final int CROSS = 14;

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
			"Crosshair", "Crosshair" ); //$NON-NLS-1$ //$NON-NLS-2$

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
			"Triangle", "Triangle" ); //$NON-NLS-1$ //$NON-NLS-2$

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
	public static final MarkerType BOX_LITERAL = new MarkerType( BOX,
			"Box", "Box" ); //$NON-NLS-1$ //$NON-NLS-2$

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
			"Circle", "Circle" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Icon</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ICON
	 * @generated
	 * @ordered
	 */
	public static final MarkerType ICON_LITERAL = new MarkerType( ICON,
			"Icon", "Icon" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Nabla</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NABLA
	 * @generated
	 * @ordered
	 */
	public static final MarkerType NABLA_LITERAL = new MarkerType( NABLA,
			"Nabla", "Nabla" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Diamond</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DIAMOND
	 * @generated
	 * @ordered
	 */
	public static final MarkerType DIAMOND_LITERAL = new MarkerType( DIAMOND,
			"Diamond", "Diamond" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Four Diamonds</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOUR_DIAMONDS
	 * @generated
	 * @ordered
	 */
	public static final MarkerType FOUR_DIAMONDS_LITERAL = new MarkerType( FOUR_DIAMONDS,
			"FourDiamonds", "Four_Diamonds" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Button</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BUTTON
	 * @generated
	 * @ordered
	 */
	public static final MarkerType BUTTON_LITERAL = new MarkerType( BUTTON,
			"Button", "Button" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Semi Circle</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SEMI_CIRCLE
	 * @generated
	 * @ordered
	 */
	public static final MarkerType SEMI_CIRCLE_LITERAL = new MarkerType( SEMI_CIRCLE,
			"SemiCircle", "Semi_Circle" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Hexagon</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #HEXAGON
	 * @generated
	 * @ordered
	 */
	public static final MarkerType HEXAGON_LITERAL = new MarkerType( HEXAGON,
			"Hexagon", "Hexagon" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Rectangle</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RECTANGLE
	 * @generated
	 * @ordered
	 */
	public static final MarkerType RECTANGLE_LITERAL = new MarkerType( RECTANGLE,
			"Rectangle", "Rectangle" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Star</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STAR
	 * @generated
	 * @ordered
	 */
	public static final MarkerType STAR_LITERAL = new MarkerType( STAR,
			"Star", "Star" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Column</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #COLUMN
	 * @generated
	 * @ordered
	 */
	public static final MarkerType COLUMN_LITERAL = new MarkerType( COLUMN,
			"Column", "Column" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Cross</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CROSS
	 * @generated
	 * @ordered
	 */
	public static final MarkerType CROSS_LITERAL = new MarkerType( CROSS,
			"Cross", "Cross" ); //$NON-NLS-1$ //$NON-NLS-2$

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
			NABLA_LITERAL,
			DIAMOND_LITERAL,
			FOUR_DIAMONDS_LITERAL,
			BUTTON_LITERAL,
			SEMI_CIRCLE_LITERAL,
			HEXAGON_LITERAL,
			RECTANGLE_LITERAL,
			STAR_LITERAL,
			COLUMN_LITERAL,
			CROSS_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Marker Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static MarkerType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			MarkerType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MarkerType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			MarkerType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Marker Type</b></em>' literal with the specified integer value.
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
			case NABLA :
				return NABLA_LITERAL;
			case DIAMOND :
				return DIAMOND_LITERAL;
			case FOUR_DIAMONDS :
				return FOUR_DIAMONDS_LITERAL;
			case BUTTON :
				return BUTTON_LITERAL;
			case SEMI_CIRCLE :
				return SEMI_CIRCLE_LITERAL;
			case HEXAGON :
				return HEXAGON_LITERAL;
			case RECTANGLE :
				return RECTANGLE_LITERAL;
			case STAR :
				return STAR_LITERAL;
			case COLUMN :
				return COLUMN_LITERAL;
			case CROSS :
				return CROSS_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private MarkerType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //MarkerType
