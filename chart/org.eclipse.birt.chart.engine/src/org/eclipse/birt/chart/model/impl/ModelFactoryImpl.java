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

package org.eclipse.birt.chart.model.impl;

import org.eclipse.birt.chart.model.*;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory </b>. <!--
 * end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory
{

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModelFactory init( )
	{
		try
		{
			ModelFactory theModelFactory = (ModelFactory) EPackage.Registry.INSTANCE.getEFactory( "http://www.birt.eclipse.org/ChartModel" ); //$NON-NLS-1$ 
			if ( theModelFactory != null )
			{
				return theModelFactory;
			}
		}
		catch ( Exception exception )
		{
			EcorePlugin.INSTANCE.log( exception );
		}
		return new ModelFactoryImpl( );
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public ModelFactoryImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create( EClass eClass )
	{
		switch ( eClass.getClassifierID( ) )
		{
			case ModelPackage.CHART :
				return createChart( );
			case ModelPackage.CHART_WITH_AXES :
				return createChartWithAxes( );
			case ModelPackage.CHART_WITHOUT_AXES :
				return createChartWithoutAxes( );
			case ModelPackage.DIAL_CHART :
				return createDialChart( );
			default :
				throw new IllegalArgumentException( "The class '" + eClass.getName( ) + "' is not a valid classifier" ); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Chart createChart( )
	{
		ChartImpl chart = new ChartImpl( );
		return chart;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChartWithAxes createChartWithAxes( )
	{
		ChartWithAxesImpl chartWithAxes = new ChartWithAxesImpl( );
		return chartWithAxes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ChartWithoutAxes createChartWithoutAxes( )
	{
		ChartWithoutAxesImpl chartWithoutAxes = new ChartWithoutAxesImpl( );
		return chartWithoutAxes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DialChart createDialChart( )
	{
		DialChartImpl dialChart = new DialChartImpl( );
		return dialChart;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPackage getModelPackage( )
	{
		return (ModelPackage) getEPackage( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static ModelPackage getPackage( )
	{
		return ModelPackage.eINSTANCE;
	}

} // ModelFactoryImpl
