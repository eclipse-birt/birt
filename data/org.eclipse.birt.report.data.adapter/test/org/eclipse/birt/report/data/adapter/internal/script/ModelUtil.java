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

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

import com.ibm.icu.util.ULocale;


public class ModelUtil
{
	
	public static ComputedColumnHandle createComputedColumnHandle( ) throws SemanticException
	{
		// Create report design with several report parameters
		SessionHandle session = DesignEngine.newSession( ULocale.getDefault());
		ReportDesignHandle designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory( );
		ComputedColumn column = new ComputedColumn( );
		column.setName( "test" );
		ComputedColumnHandle handle = factory.newTableItem( "a" ).addColumnBinding( column, false );
		return handle;
	}
	

	/**
	 * Create simple cube handle.
	 * 
	 * @param module
	 * @return
	 */

	public static CubeHandle prepareCube1( ) throws SemanticException
	{
		// Create report design with several report parameters
		SessionHandle session = DesignEngine.newSession( ULocale.getDefault());
		ReportDesignHandle designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory( );

		// create cube
		CubeHandle cubeHandle = factory.newTabularCube( "timeCube" );//$NON-NLS-1$
		designHandle.getCubes( ).add( cubeHandle );

		DimensionHandle dimensionHandle = factory.newTabularDimension( "Customer" );//$NON-NLS-1$
		cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle );

		HierarchyHandle hierarchyHandle = factory.newTabularHierarchy( "Hierarchy" );//$NON-NLS-1$
		dimensionHandle.add( DimensionHandle.HIERARCHIES_PROP, hierarchyHandle );
		dimensionHandle.setDefaultHierarchy( hierarchyHandle );

		LevelHandle levelHandle = factory.newTabularLevel( dimensionHandle,
				"CUSTOMER_SEX" );//$NON-NLS-1$
		hierarchyHandle.add( HierarchyHandle.LEVELS_PROP, levelHandle );

		levelHandle = factory.newTabularLevel( dimensionHandle,
				"CUSTOMER_REGION" );//$NON-NLS-1$
		hierarchyHandle.add( HierarchyHandle.LEVELS_PROP, levelHandle );

		DimensionHandle dimensionHandle2 = factory.newTabularDimension( "TimeDimension" );//$NON-NLS-1$
		cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle2 );
		dimensionHandle2.setTimeType( true );
		
		HierarchyHandle hierarchyHandle2 = factory.newTabularHierarchy( "Hierarchy2" );//$NON-NLS-1$
		dimensionHandle2.add( DimensionHandle.HIERARCHIES_PROP,
				hierarchyHandle2 );
		dimensionHandle2.setDefaultHierarchy( hierarchyHandle2 );


		LevelHandle levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"year" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

		levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"quarter" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

		levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"month" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

		MeasureGroupHandle groupHandle = factory.newTabularMeasureGroup( "measure group" );//$NON-NLS-1$
		cubeHandle.add( CubeHandle.MEASURE_GROUPS_PROP, groupHandle );

		MeasureHandle measureHandle = factory.newTabularMeasure( "QUANTITY_PRICE" );//$NON-NLS-1$
		groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );

		return cubeHandle;
	}
	

	/**
	 * Create simple cube handle.
	 * 
	 * @param module
	 * @return
	 */

	public static CubeHandle prepareCube2( ) throws SemanticException
	{
		// Create report design with several report parameters
		SessionHandle session = DesignEngine.newSession( ULocale.getDefault());
		ReportDesignHandle designHandle = session.createDesign();
		ElementFactory factory = designHandle.getElementFactory( );

		// create cube
		CubeHandle cubeHandle = factory.newTabularCube( "timeCube" );//$NON-NLS-1$
		designHandle.getCubes( ).add( cubeHandle );

		DimensionHandle dimensionHandle = factory.newTabularDimension( "Customer" );//$NON-NLS-1$
		cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle );

		HierarchyHandle hierarchyHandle = factory.newTabularHierarchy( "Hierarchy" );//$NON-NLS-1$
		dimensionHandle.add( DimensionHandle.HIERARCHIES_PROP, hierarchyHandle );
		dimensionHandle.setDefaultHierarchy( hierarchyHandle );

		LevelHandle levelHandle = factory.newTabularLevel( dimensionHandle,
				"CUSTOMER_SEX" );//$NON-NLS-1$
		hierarchyHandle.add( HierarchyHandle.LEVELS_PROP, levelHandle );

		levelHandle = factory.newTabularLevel( dimensionHandle,
				"CUSTOMER_REGION" );//$NON-NLS-1$
		hierarchyHandle.add( HierarchyHandle.LEVELS_PROP, levelHandle );

		DimensionHandle dimensionHandle2 = factory.newTabularDimension( "TimeDimension" );//$NON-NLS-1$
		cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle2 );
		dimensionHandle2.setTimeType( true );
		
		HierarchyHandle hierarchyHandle2 = factory.newTabularHierarchy( "Hierarchy2" );//$NON-NLS-1$
		dimensionHandle2.add( DimensionHandle.HIERARCHIES_PROP,
				hierarchyHandle2 );
		dimensionHandle2.setDefaultHierarchy( hierarchyHandle2 );


		LevelHandle levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"year" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

		levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"quarter" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

		levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"month" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

		levelHandle2 = factory.newTabularLevel( dimensionHandle2,
				"day-of-year" );//$NON-NLS-1$
		levelHandle2.setDateTimeLevelType( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR );
		hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );
		
		MeasureGroupHandle groupHandle = factory.newTabularMeasureGroup( "measure group" );//$NON-NLS-1$
		cubeHandle.add( CubeHandle.MEASURE_GROUPS_PROP, groupHandle );

		MeasureHandle measureHandle = factory.newTabularMeasure( "QUANTITY_PRICE" );//$NON-NLS-1$
		groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );

		return cubeHandle;
	}
}
