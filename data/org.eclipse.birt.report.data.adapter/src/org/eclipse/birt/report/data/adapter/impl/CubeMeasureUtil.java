/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.DerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.MeasureDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;

public class CubeMeasureUtil
{

	private static IModelAdapter getModelAdapter( ) throws BirtException
	{
		return new DataModelAdapter( new DataSessionContext( DataSessionContext.MODE_GENERATION ) );
	}

	/**
	 * Check whether the derived measure reference is valid.
	 * 
	 * @param cubeHandle CubeHandle
	 * @throws BirtException If invalid measure reference or recursive measrue reference is detected.
	 */
	public static void validateDerivedMeasures( CubeHandle cubeHandle )
			throws BirtException
	{
		Map<String, IMeasureDefinition> measures = new HashMap<String, IMeasureDefinition>( );
		Map<String, IDerivedMeasureDefinition> calculatedMeasures = new HashMap<String, IDerivedMeasureDefinition>( );

		List measureGroups = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
		for ( int i = 0; i < measureGroups.size( ); i++ )
		{
			MeasureGroupHandle mgh = (MeasureGroupHandle) measureGroups.get( i );
			List measureGroup = mgh.getContents( MeasureGroupHandle.MEASURES_PROP );
			for ( int j = 0; j < measureGroup.size( ); j++ )
			{
				MeasureHandle mHandle = (MeasureHandle) measureGroup.get( j );
				if ( mHandle.isCalculated( ) )
				{
					DerivedMeasureDefinition m = new DerivedMeasureDefinition( mHandle.getName( ),
							DataAdapterUtil.adaptModelDataType( mHandle.getDataType( ) ),
							getModelAdapter( ).adaptExpression( (Expression) mHandle.getExpressionProperty( IMeasureModel.MEASURE_EXPRESSION_PROP )
									.getValue( ),
									ExpressionLocation.CUBE ) );
					calculatedMeasures.put( mHandle.getName( ), m );
				}
				else
				{
					MeasureDefinition m = new MeasureDefinition( mHandle.getName( ) );
					m.setAggrFunction( DataAdapterUtil.getRollUpAggregationName( mHandle.getFunction( ) ) );
					measures.put( m.getName( ), m );
				}
			}
		}

		for ( Map.Entry<String, IDerivedMeasureDefinition> e : calculatedMeasures.entrySet( ) )
		{
			List<String> resolving = new ArrayList<String>( );
			checkDerivedMeasure( e.getValue( ),
					resolving,
					measures,
					calculatedMeasures, cubeHandle );
		}
	}

	private static void checkDerivedMeasure(
			IDerivedMeasureDefinition dmeasure, List<String> resolving,
			Map<String, IMeasureDefinition> measures,
			Map<String, IDerivedMeasureDefinition> calculatedMeasure, CubeHandle cubeHandle )
			throws DataException
	{
		List referencedMeasures = ExpressionCompilerUtil.extractColumnExpression( dmeasure.getExpression( ),
				ExpressionUtil.MEASURE_INDICATOR );
		resolving.add( dmeasure.getName( ) );

		for ( int i = 0; i < referencedMeasures.size( ); i++ )
		{
			String measureName = referencedMeasures.get( i ).toString( );
			if ( measures.containsKey( measureName ) )
			{
				continue;
			}
			else
			{
				if ( !calculatedMeasure.containsKey( measureName ) )
				{
					MeasureHandle measureHandle = cubeHandle.getMeasure( measureName );
					if ( measureHandle == null )
						throw new DataException( AdapterResourceHandle.getInstance( )
								.getMessage( ResourceConstants.CUBE_DERIVED_MEASURE_INVALID_REF,
										new Object[]{
												dmeasure.getName( ),
												measureName
										} ) );
					
					throw new DataException( AdapterResourceHandle.getInstance( )
							.getMessage( ResourceConstants.CUBE_DERIVED_MEASURE_RESOLVE_ERROR,
									new Object[]{
										resolving.get( 0 )
									} ) );
				}

				for ( int j = 0; j < resolving.size( ); j++ )
				{
					if ( measureName.equals( resolving.get( j ) ) )
					{
						resolving.add( measureName );
						throw new DataException( AdapterResourceHandle.getInstance( )
								.getMessage( ResourceConstants.CUBE_DERIVED_MEASURE_RECURSIVE_REF,
										new Object[]{
												resolving.get( 0 ),
												resolving.toString( )
										} ) );
					}
				}

				checkDerivedMeasure( calculatedMeasure.get( measureName ),
						resolving,
						measures,
						calculatedMeasure, cubeHandle );
			}
		}

		resolving.remove( resolving.size( ) - 1 );
	}
}
