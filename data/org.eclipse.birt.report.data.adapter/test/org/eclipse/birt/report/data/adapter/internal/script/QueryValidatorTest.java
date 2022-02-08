/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.internal.script;

import junit.framework.TestCase;

import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.timefunction.TimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.impl.QueryValidator;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;


public class QueryValidatorTest extends TestCase
{
	private CubeHandle cube1;//with year, quarter, month
	
	public void testValidate1( ) throws SemanticException, DataException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		ICubeQueryDefinition query = new CubeQueryDefinition( "timeCube" );
		IEdgeDefinition edge = query.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dimension = edge.createDimension( "TimeDimension" );
		IHierarchyDefinition hierarchy = dimension.createHierarchy( "hierarchy" );
		hierarchy.createLevel( "year" );
		hierarchy.createLevel( "quarter" );

		IBinding binding = new Binding("timeBinding");
		TimeFunction function = new TimeFunction( );
		TimePeriod period = new TimePeriod( 0, TimePeriodType.MONTH );
		function.setBaseTimePeriod( period );
		function.setTimeDimension( "TimeDimension" );
		binding.setTimeFunction( function );
		
		query.addBinding( binding );
		try
		{
			QueryValidator.validateTimeFunctionInCubeQuery( query, cube1 );
			fail( "should not get here" );
		}
		catch ( AdapterException ex )
		{
			assertTrue( ex.getErrorCode( )
					.equals( ResourceConstants.MISS_TIME_TYPE_LEVEL ) );
		}
	}
	
	public void testValidate2( ) throws SemanticException, DataException, AdapterException
	{
		cube1 = ModelUtil.prepareCube1( );
		ICubeQueryDefinition query = new CubeQueryDefinition( "timeCube" );
		IEdgeDefinition edge = query.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dimension = edge.createDimension( "Customer" );
		IHierarchyDefinition hierarchy = dimension.createHierarchy( "hierarchy" );
		hierarchy.createLevel( "CUSTOMER_SEX" );

		IBinding binding = new Binding("timeBinding");
		TimeFunction function = new TimeFunction( );
		TimePeriod period = new TimePeriod( 0, TimePeriodType.DAY );
		function.setBaseTimePeriod( period );
		binding.setTimeFunction( function );
		function.setTimeDimension( "TimeDimension" );

		query.addBinding( binding );
		try
		{
			QueryValidator.validateTimeFunctionInCubeQuery( query, cube1 );
			fail("should not get here");
		}
		catch ( AdapterException ex )
		{
			assertTrue( ex.getErrorCode( )
					.equals( ResourceConstants.CUBE_QUERY_MISS_LEVEL ) );
		}
	}
}
