/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core;

import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

import com.ibm.icu.util.ULocale;

/**
 * Base test case.
 *
 */

public class BaseTestCase extends TestCase
{
	/**
	 * design handle
	 */
	protected ReportDesignHandle designHandle = null;
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	

	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
		if( designHandle != null )
		{
			designHandle.close( );
			designHandle = null;
		}
	}

	
	/**
	 * Opens report design.
	 * @param fileName
	 * @throws Exception
	 */
	
	protected void openDesign( String fileName ) throws Exception
	{
		ThreadResources.setLocale( ULocale.ENGLISH );
		IDesignEngine designEngine = new DesignEngine( new DesignConfig( ) );
		MetaDataDictionary.reset( );
		// initialize the metadata.

		designEngine.getMetaData( );

		SessionHandle sh = designEngine
				.newSessionHandle( ULocale.getDefault( ) );
		designHandle = sh.openDesign( getResource( fileName ) );
	}
	
	/**
	 * Create report design.
	 * @throws Exception
	 */
	
	protected void createDesign() throws Exception
	{
		ThreadResources.setLocale( ULocale.ENGLISH );
		IDesignEngine designEngine = new DesignEngine( new DesignConfig( ) );
		MetaDataDictionary.reset( );
		// initialize the metadata.

		designEngine.getMetaData( );

		SessionHandle sh = designEngine
				.newSessionHandle( ULocale.getDefault( ) );
		designHandle = sh.createDesign( );
	}
	
	/**
	 * Gets resource file path.
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	
	private String getResource( String fileName ) throws Exception
	{
		if( fileName == null )
			return null;
		URL url = getClass( ).getResource( fileName );
		if( url == null )
			return null;
		return url.toString() ;
	}
	
	/**
	 * Create simple crosstab handle.
	 * 
	 * Row is Customer / column is Product
	 * crosstab diagram is like :
	 *  
	 *                      product_type       product_name
	 * customer_sex         
	 * customer_region     quantity_price    quantity       quantity_number    quantity_size
	 * 
	 * @param module
	 * @return
	 */

	protected CrosstabReportItemHandle createSimpleCrosstab( ModuleHandle module )
	{
		try
		{
			ElementFactory factory = designHandle.getElementFactory( );

			// create cube
			CubeHandle cubeHandle = factory.newTabularCube( "Cube_Test_1" );//$NON-NLS-1$
			designHandle.getCubes( ).add( cubeHandle );

			DimensionHandle dimensionHandle = factory
					.newTabularDimension( "Customer" );//$NON-NLS-1$
			cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle );

			HierarchyHandle hierarchyHandle = factory
					.newTabularHierarchy( "Hierarchy" );//$NON-NLS-1$
			dimensionHandle.add( DimensionHandle.HIERARCHIES_PROP,
					hierarchyHandle );

			LevelHandle levelHandle = factory.newTabularLevel( "CUSTOMER_SEX" );//$NON-NLS-1$
			hierarchyHandle.add( HierarchyHandle.LEVELS_PROP, levelHandle );

			levelHandle = factory.newTabularLevel( "CUSTOMER_REGION" );//$NON-NLS-1$
			hierarchyHandle.add( HierarchyHandle.LEVELS_PROP, levelHandle );

			DimensionHandle dimensionHandle2 = factory
					.newTabularDimension( "Product" );//$NON-NLS-1$
			cubeHandle.add( CubeHandle.DIMENSIONS_PROP, dimensionHandle2 );

			HierarchyHandle hierarchyHandle2 = factory
					.newTabularHierarchy( "Hierarchy2" );//$NON-NLS-1$
			dimensionHandle2.add( DimensionHandle.HIERARCHIES_PROP,
					hierarchyHandle2 );

			LevelHandle levelHandle2 = factory.newTabularLevel( "PRODUCT_TYPE" );//$NON-NLS-1$
			hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

			levelHandle2 = factory.newTabularLevel( "PRODUCT_NAME" );//$NON-NLS-1$
			hierarchyHandle2.add( HierarchyHandle.LEVELS_PROP, levelHandle2 );

			MeasureGroupHandle groupHandle = factory
					.newTabularMeasureGroup( "measure group" );//$NON-NLS-1$
			cubeHandle.add( CubeHandle.MEASURE_GROUPS_PROP, groupHandle );

			MeasureHandle measureHandle = factory
					.newTabularMeasure( "QUANTITY_PRICE" );//$NON-NLS-1$
			groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );

			measureHandle = factory.newTabularMeasure( "QUANTITY" );//$NON-NLS-1$
			groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );

			measureHandle = factory.newTabularMeasure( "QUANTITY_NUMBER" );//$NON-NLS-1$
			groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );

			measureHandle = factory.newTabularMeasure( "QUANTITY_SIZE" );//$NON-NLS-1$
			groupHandle.add( MeasureGroupHandle.MEASURES_PROP, measureHandle );

			// create cross tab
			CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil
					.getReportItem( CrosstabExtendedItemFactory
							.createCrosstabReportItem( module, cubeHandle ) );

			DimensionViewHandle dimensionViewHandle = crosstabItem
					.insertDimension( dimensionHandle,
							ICrosstabConstants.ROW_AXIS_TYPE, -1 );

			dimensionViewHandle.insertLevel( hierarchyHandle.getLevel( 0 ), -1 );
			dimensionViewHandle.insertLevel( hierarchyHandle.getLevel( 1 ), -1 );

			DimensionViewHandle dimensionViewHandle2 = crosstabItem
					.insertDimension( dimensionHandle2,
							ICrosstabConstants.COLUMN_AXIS_TYPE, -1 );

			dimensionViewHandle2.insertLevel( hierarchyHandle2.getLevel( 0 ),
					-1 );
			dimensionViewHandle2.insertLevel( hierarchyHandle2.getLevel( 1 ),
					-1 );

			crosstabItem.insertMeasure( cubeHandle
					.getMeasure( "QUANTITY_PRICE" ), -1 );//$NON-NLS-1$
			crosstabItem
					.insertMeasure( cubeHandle.getMeasure( "QUANTITY" ), -1 );//$NON-NLS-1$
			crosstabItem.insertMeasure( cubeHandle
					.getMeasure( "QUANTITY_NUMBER" ), -1 );//$NON-NLS-1$
			crosstabItem.insertMeasure(
					cubeHandle.getMeasure( "QUANTITY_SIZE" ), -1 );//$NON-NLS-1$
			return crosstabItem;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
		return null;
	}
	
}
