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
 * <em><b>Chart Dimension</b></em>', and utility methods for working with
 * them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for Chart dimensions. Any new Chart
 * dimension type needs to be added here to be supported.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getChartDimension()
 * @model
 * @generated
 */
public final class ChartDimension extends AbstractEnumerator
{

	/**
	 * The '<em><b>Two Dimensional</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL_LITERAL
	 * @model name="Two_Dimensional"
	 * @generated
	 * @ordered
	 */
	public static final int TWO_DIMENSIONAL = 0;

	/**
	 * The '<em><b>Two Dimensional With Depth</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL_WITH_DEPTH_LITERAL
	 * @model name="Two_Dimensional_With_Depth"
	 * @generated
	 * @ordered
	 */
	public static final int TWO_DIMENSIONAL_WITH_DEPTH = 1;

	/**
	 * The '<em><b>Three Dimensional</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #THREE_DIMENSIONAL_LITERAL
	 * @model name="Three_Dimensional"
	 * @generated
	 * @ordered
	 */
	public static final int THREE_DIMENSIONAL = 2;

	/**
	 * The '<em><b>Two Dimensional</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Two Dimensional</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL
	 * @generated
	 * @ordered
	 */
	public static final ChartDimension TWO_DIMENSIONAL_LITERAL = new ChartDimension( TWO_DIMENSIONAL,
			"Two_Dimensional" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Two Dimensional With Depth</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Two Dimensional With Depth</b></em>'
	 * literal object isn't clear, there really should be more of a description
	 * here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #TWO_DIMENSIONAL_WITH_DEPTH
	 * @generated
	 * @ordered
	 */
	public static final ChartDimension TWO_DIMENSIONAL_WITH_DEPTH_LITERAL = new ChartDimension( TWO_DIMENSIONAL_WITH_DEPTH,
			"Two_Dimensional_With_Depth" ); //$NON-NLS-1$

	/**
	 * The '<em><b>Three Dimensional</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Three Dimensional</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #THREE_DIMENSIONAL
	 * @generated
	 * @ordered
	 */
	public static final ChartDimension THREE_DIMENSIONAL_LITERAL = new ChartDimension( THREE_DIMENSIONAL,
			"Three_Dimensional" ); //$NON-NLS-1$

	/**
	 * An array of all the '<em><b>Chart Dimension</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static final ChartDimension[] VALUES_ARRAY = new ChartDimension[]{
			TWO_DIMENSIONAL_LITERAL,
			TWO_DIMENSIONAL_WITH_DEPTH_LITERAL,
			THREE_DIMENSIONAL_LITERAL,
	};

	/**
	 * A public read-only list of all the '<em><b>Chart Dimension</b></em>'
	 * enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Chart Dimension</b></em>' literal with the
	 * specified name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChartDimension get( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			ChartDimension result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Chart Dimension</b></em>' literal with the
	 * specified value. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static ChartDimension get( int value )
	{
		switch ( value )
		{
			case TWO_DIMENSIONAL :
				return TWO_DIMENSIONAL_LITERAL;
			case TWO_DIMENSIONAL_WITH_DEPTH :
				return TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
			case THREE_DIMENSIONAL :
				return THREE_DIMENSIONAL_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	private ChartDimension( int value, String name )
	{
		super( value, name );
	}

} //ChartDimension
