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

package org.eclipse.birt.chart.model.type.impl;

import java.util.Iterator;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Area Series</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class AreaSeriesImpl extends LineSeriesImpl implements AreaSeries
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected AreaSeriesImpl( )
	{
		super( );
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass( )
	{
		return TypePackage.Literals.AREA_SERIES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#translateFrom(org.eclipse.birt.chart.model.component.Series,
	 *      int, org.eclipse.birt.chart.model.Chart)
	 */
	public void translateFrom( Series series, int iSeriesDefinitionIndex,
			Chart chart )
	{
		super.translateFrom( series, iSeriesDefinitionIndex, chart );

		for ( Iterator itr = getMarkers( ).iterator( ); itr.hasNext( ); )
		{
			Marker mk = (Marker) itr.next( );
			mk.setVisible( false );
		}
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static Series create( )
	{
		final AreaSeries as = TypeFactory.eINSTANCE.createAreaSeries( );
		( (AreaSeriesImpl) as ).initialize( );
		return as;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.impl.SeriesImpl#initialize()
	 */
	protected void initialize( )
	{
		super.initialize( );

		for ( Iterator itr = getMarkers( ).iterator( ); itr.hasNext( ); )
		{
			Marker mk = (Marker) itr.next( );
			mk.setVisible( false );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "AreaSeriesImpl.displayName" ); //$NON-NLS-1$	
	}

} // AreaSeriesImpl
