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
import java.util.Map;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.CursorImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.DataSetImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
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
	@Override
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

	private static AreaSeries copyInstanceThis( AreaSeries src )
	{
		if ( src == null )
		{
			return null;
		}

		AreaSeriesImpl dest = new AreaSeriesImpl( );

		if ( src.getLabel( ) != null )
		{
			dest.setLabel( LabelImpl.copyInstance( src.getLabel( ) ) );
		}

		if ( src.getDataDefinition( ) != null )
		{
			EList<Query> list = dest.getDataDefinition( );
			for ( Query element : src.getDataDefinition( ) )
			{
				list.add( QueryImpl.copyInstance( element ) );
			}
		}

		if ( src.getDataPoint( ) != null )
		{
			dest.setDataPoint( DataPointImpl.copyInstance( src.getDataPoint( ) ) );
		}

		if ( src.getDataSets( ) != null )
		{
			EMap<String, DataSet> map = dest.getDataSets( );
			for ( Map.Entry<String, DataSet> entry : src.getDataSets( )
					.entrySet( ) )
			{
				map.put( entry.getKey( ),
						DataSetImpl.copyInstance( entry.getValue( ) ) );
			}
		}

		if ( src.getTriggers( ) != null )
		{
			EList<Trigger> list = dest.getTriggers( );
			for ( Trigger element : src.getTriggers( ) )
			{
				list.add( TriggerImpl.copyInstance( element ) );
			}
		}

		if ( src.getCurveFitting( ) != null )
		{
			dest.setCurveFitting( CurveFittingImpl.copyInstance( src.getCurveFitting( ) ) );
		}

		if ( src.getCursor( ) != null )
		{
			dest.setCursor( CursorImpl.copyInstance( src.getCursor( ) ) );
		}

		if ( src.getMarkers( ) != null )
		{
			EList<Marker> list = dest.getMarkers( );
			for ( Marker element : src.getMarkers( ) )
			{
				list.add( MarkerImpl.copyInstance( element ) );
			}
		}

		if ( src.getMarker( ) != null )
		{
			dest.setMarker( MarkerImpl.copyInstance( src.getMarker( ) ) );
		}

		if ( src.getLineAttributes( ) != null )
		{
			dest.setLineAttributes( LineAttributesImpl.copyInstance( src.getLineAttributes( ) ) );
		}

		if ( src.getShadowColor( ) != null )
		{
			dest.setShadowColor( ColorDefinitionImpl.copyInstance( src.getShadowColor( ) ) );
		}

		dest.visible = src.isVisible( );
		dest.visibleESet = src.isSetVisible( );
		dest.seriesIdentifier = src.getSeriesIdentifier( );
		dest.labelPosition = src.getLabelPosition( );
		dest.labelPositionESet = src.isSetLabelPosition( );
		dest.stacked = src.isStacked( );
		dest.stackedESet = src.isSetStacked( );
		dest.translucent = src.isTranslucent( );
		dest.translucentESet = src.isSetTranslucent( );
		dest.paletteLineColor = src.isPaletteLineColor( );
		dest.paletteLineColorESet = src.isSetPaletteLineColor( );
		dest.curve = src.isCurve( );
		dest.curveESet = src.isSetCurve( );
		dest.connectMissingValue = src.isConnectMissingValue( );
		dest.connectMissingValueESet = src.isSetConnectMissingValue( );

		return dest;
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 * 
	 * @param src
	 * @return
	 */
	public static AreaSeries copyInstance( AreaSeries src )
	{
		if ( src == null )
		{
			return null;
		}

		if ( src instanceof DifferenceSeries )
		{
			return DifferenceSeriesImpl.copyInstance( (DifferenceSeries) src );
		}
		else
		{
			return copyInstanceThis( src );
		}
	}

} // AreaSeriesImpl
