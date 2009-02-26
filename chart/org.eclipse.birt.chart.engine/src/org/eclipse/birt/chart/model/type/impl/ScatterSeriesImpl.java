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

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
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
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Scatter Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class ScatterSeriesImpl extends LineSeriesImpl implements ScatterSeries
{

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ScatterSeriesImpl( )
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
		return TypePackage.Literals.SCATTER_SERIES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
	 */
	public final boolean canBeStacked( )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#canParticipateInCombination()
	 */
	public final boolean canParticipateInCombination( )
	{
		return false;
	}

	/**
	 * A convenience method to create an initialized 'Series' instance
	 * 
	 * @return
	 */
	public static Series create( )
	{
		final ScatterSeries ss = TypeFactory.eINSTANCE.createScatterSeries( );
		( (ScatterSeriesImpl) ss ).initialize( );
		return ss;
	}

	/**
	 * Initializes all member variables within this object recursively
	 * 
	 * Note: Manually written
	 */
	protected void initialize( )
	{
		super.initialize( );
		getLineAttributes( ).setVisible( false );

		getMarkers( ).get( 0 ).setType( MarkerType.CROSSHAIR_LITERAL );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.component.Series#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "ScatterSeriesImpl.displayName" ); //$NON-NLS-1$
	}

	private static ScatterSeries copyInstanceThis( ScatterSeries src )
	{
		if ( src == null )
		{
			return null;
		}

		ScatterSeriesImpl dest = new ScatterSeriesImpl( );

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
			map.putAll( src.getDataSets( ) );
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
	public static ScatterSeries copyInstance( ScatterSeries src )
	{
		if ( src == null )
		{
			return null;
		}

		if ( src instanceof BubbleSeries )
		{
			return BubbleSeriesImpl.copyInstance( (BubbleSeries) src );
		}
		else
		{
			return copyInstanceThis( src );
		}
	}

} // ScatterSeriesImpl
