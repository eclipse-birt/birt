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
 * <em><b>Data Point Component Type</b></em>', and utility methods for
 * working with them. <!-- end-user-doc --> <!-- begin-model-doc -->
 * 
 * This type defines the allowed values for data point components.
 * 
 * <!-- end-model-doc -->
 * 
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getDataPointComponentType()
 * @model
 * @generated
 */
public final class DataPointComponentType extends AbstractEnumerator
{

	/**
	 * The '<em><b>Base Value</b></em>' literal value.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @see #BASE_VALUE_LITERAL
	 * @model name="BaseValue" literal="Base_Value"
	 * @generated
	 * @ordered
	 */
	public static final int BASE_VALUE = 0;

	/**
	 * The '<em><b>Orthogonal Value</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #ORTHOGONAL_VALUE_LITERAL
	 * @model name="Orthogonal_Value"
	 * @generated
	 * @ordered
	 */
	public static final int ORTHOGONAL_VALUE = 1;

	/**
	 * The '<em><b>Series Value</b></em>' literal value. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #SERIES_VALUE_LITERAL
	 * @model name="Series_Value"
	 * @generated
	 * @ordered
	 */
	public static final int SERIES_VALUE = 2;

	/**
	 * The '<em><b>Percentile Orthogonal Value</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Percentile Orthogonal Value</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PERCENTILE_ORTHOGONAL_VALUE_LITERAL
	 * @model name="PercentileOrthogonalValue" literal="Percentile_Orthogonal_Value"
	 * @generated
	 * @ordered
	 */
	public static final int PERCENTILE_ORTHOGONAL_VALUE = 3;

	/**
	 * The '<em><b>Base Value</b></em>' literal object.
	 * <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of '<em><b>Base Value</b></em>' literal object isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BASE_VALUE
	 * @generated
	 * @ordered
	 */
	public static final DataPointComponentType BASE_VALUE_LITERAL = new DataPointComponentType( BASE_VALUE,
			"BaseValue", "Base_Value" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Orthogonal Value</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Orthogonal Value</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #ORTHOGONAL_VALUE
	 * @generated
	 * @ordered
	 */
	public static final DataPointComponentType ORTHOGONAL_VALUE_LITERAL = new DataPointComponentType( ORTHOGONAL_VALUE,
			"OrthogonalValue", "Orthogonal_Value" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Series Value</b></em>' literal object. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Series Value</b></em>' literal object
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @see #SERIES_VALUE
	 * @generated
	 * @ordered
	 */
	public static final DataPointComponentType SERIES_VALUE_LITERAL = new DataPointComponentType( SERIES_VALUE,
			"SeriesValue", "Series_Value" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Percentile Orthogonal Value</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PERCENTILE_ORTHOGONAL_VALUE
	 * @generated
	 * @ordered
	 */
	public static final DataPointComponentType PERCENTILE_ORTHOGONAL_VALUE_LITERAL = new DataPointComponentType( PERCENTILE_ORTHOGONAL_VALUE,
			"PercentileOrthogonalValue", "Percentile_Orthogonal_Value" ); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * An array of all the '<em><b>Data Point Component Type</b></em>' enumerators.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	private static final DataPointComponentType[] VALUES_ARRAY = new DataPointComponentType[]{
			BASE_VALUE_LITERAL,
			ORTHOGONAL_VALUE_LITERAL,
			SERIES_VALUE_LITERAL,
			PERCENTILE_ORTHOGONAL_VALUE_LITERAL,
	};

	/**
	 * A public read-only list of all the '
	 * <em><b>Data Point Component Type</b></em>' enumerators. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList( Arrays.asList( VALUES_ARRAY ) );

	/**
	 * Returns the '<em><b>Data Point Component Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static DataPointComponentType get( String literal )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			DataPointComponentType result = VALUES_ARRAY[i];
			if ( result.toString( ).equals( literal ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Data Point Component Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DataPointComponentType getByName( String name )
	{
		for ( int i = 0; i < VALUES_ARRAY.length; ++i )
		{
			DataPointComponentType result = VALUES_ARRAY[i];
			if ( result.getName( ).equals( name ) )
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Data Point Component Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static DataPointComponentType get( int value )
	{
		switch ( value )
		{
			case BASE_VALUE :
				return BASE_VALUE_LITERAL;
			case ORTHOGONAL_VALUE :
				return ORTHOGONAL_VALUE_LITERAL;
			case SERIES_VALUE :
				return SERIES_VALUE_LITERAL;
			case PERCENTILE_ORTHOGONAL_VALUE :
				return PERCENTILE_ORTHOGONAL_VALUE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private DataPointComponentType( int value, String name, String literal )
	{
		super( value, name, literal );
	}

} //DataPointComponentType
