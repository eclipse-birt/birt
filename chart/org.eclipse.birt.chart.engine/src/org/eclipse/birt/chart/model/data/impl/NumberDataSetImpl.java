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

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Number Data Set</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class NumberDataSetImpl extends DataSetImpl implements NumberDataSet
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected NumberDataSetImpl( )
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
		return DataPackage.Literals.NUMBER_DATA_SET;
	}

	/**
	 * A convenience method to create an initialized 'NumberDataSet' instance
	 * 
	 * @param oValues
	 *            The Collection (of Double(s)), Double[] or double[] of values associated with this dataset
	 * 
	 * @return
	 */
	public static final NumberDataSet create( Object oValues )
	{
		final NumberDataSet nds = DataFactory.eINSTANCE.createNumberDataSet( );
		( (NumberDataSetImpl) nds ).initialize( );
		nds.setValues( oValues );
		return nds;
	}

	/**
	 * This method performs any initialization of the instance when created
	 * 
	 * Note: Manually written
	 */
	protected void initialize( )
	{
	}

	/**
	 * @generated
	 */
	public NumberDataSet copyInstance( )
	{
		NumberDataSetImpl dest = new NumberDataSetImpl( );
		dest.set( this );
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set( NumberDataSet src )
	{

		super.set( src );

	}

} //NumberDataSetImpl
