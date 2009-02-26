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

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class DataElementImpl extends EObjectImpl implements DataElement
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected DataElementImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass( )
	{
		return DataPackage.Literals.DATA_ELEMENT;
	}

	private static DataElement copyInstanceThis( DataElement src )
	{
		if ( src == null )
		{
			return null;
		}

		DataElementImpl dest = new DataElementImpl( );

		return dest;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static DataElement copyInstance( DataElement src )
	{
		if ( src == null )
		{
			return null;
		}

		if ( src instanceof DateTimeDataElement )
		{
			return DateTimeDataElementImpl.copyInstance( (DateTimeDataElement) src );
		}
		else if ( src instanceof NumberDataElement )
		{
			return NumberDataElementImpl.copyInstance( (NumberDataElement) src );
		}
		else
		{
			return copyInstanceThis( src );
		}

	}

} //DataElementImpl
