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

package org.eclipse.birt.report.item.crosstab.internal.ui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.re.CubeElementFactory;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * 
 */

public class CrosstabUIHelper
{

	public static String CROSSTAB_IMAGE = "icons/pal/crosstab.gif";//$NON-NLS-1$
	public static String MEASURE_IMAGE = "icons/pal/data.gif";//$NON-NLS-1$
	public static String COLUMNS_AREA_IMAGE = "icons/pal/column-area.gif";//$NON-NLS-1$
	public static String ROWS_AREA_IMAGE = "icons/pal/row-area.gif";//$NON-NLS-1$
	public static String DETAIL_AREA_IMAGE = "icons/pal/detail-area.gif";//$NON-NLS-1$
	public static String LEVEL_IMAGE = "icons/pal/level.gif";//$NON-NLS-1$
	public static String CELL_IMAGE = "icons/pal/cell.gif";//$NON-NLS-1$
	public static String DETAIL_IMAGE = "icons/pal/details.gif";//$NON-NLS-1$
	public static String HEADER_IMAGE = "icons/pal/header.gif";//$NON-NLS-1$
	public static String AGGREGATION_IMAGE = "icons/pal/aggregation.gif";//$NON-NLS-1$
	public static String LEVEL_AGGREGATION = "icons/pal/cell-level-aggregation.gif";//$NON-NLS-1$
	public static String SHOW_HIDE_LECEL = "icons/pal/show-hide-level.gif";//$NON-NLS-1$
	public static String LEVEL_ARROW = "icons/pal/level-arrow.gif";//$NON-NLS-1$

	private static Image createImage( String sPluginRelativePath )
	{
		Image img = null;
		try
		{
			try
			{
				img = new Image( Display.getCurrent( ),
						getURL( sPluginRelativePath ).openStream( ) );
			}
			catch ( MalformedURLException e1 )
			{
				img = new Image( Display.getCurrent( ),
						new FileInputStream( getURL( sPluginRelativePath ).toString( ) ) );
			}
		}
		catch ( FileNotFoundException e )
		{

		}
		catch ( IOException e )
		{

		}

		// If still can't load, return a dummy image.
		if ( img == null )
		{
			img = new Image( Display.getCurrent( ), 1, 1 );
		}
		return img;
	}

	/**
	 * This method returns an URL for a resource given its plugin relative path.
	 * It is intended to be used to abstract out the usage of the UI as a plugin
	 * or standalone component when it comes to accessing resources.
	 * 
	 * @param sPluginRelativePath
	 *            The path to the resource relative to the plugin location.
	 * @return URL representing the location of the resource.
	 */
	public static URL getURL( String sPluginRelativePath )
	{
		URL url = null;

		try
		{
			url = new URL( CrosstabPlugin.getDefault( )
					.getBundle( )
					.getEntry( "/" ), sPluginRelativePath ); //$NON-NLS-1$
		}
		catch ( MalformedURLException e )
		{
		}

		return url;
	}

	/**
	 * This is a convenience method to get an imgIcon from a URL.
	 * 
	 * @param sPluginRelativePath
	 *            The URL for the imgIcon.
	 * @return The imgIcon represented by the given URL.
	 * @see #setImageCached( boolean )
	 */
	public static Image getImage( String sPluginRelativePath )
	{
		ImageRegistry registry = JFaceResources.getImageRegistry( );
		Image image = registry.get( sPluginRelativePath );
		if ( image == null )
		{
			image = createImage( sPluginRelativePath );
			registry.put( sPluginRelativePath, image );
		}
		return image;
	}

	private static final String LABEL_NAME = Messages.getString( "AddSubTotalAction.LabelName" );//$NON-NLS-1$

	public static void CreateGrandTotalLabel( CrosstabCellHandle cellHandle )
			throws SemanticException
	{
		LabelHandle dataHandle = DesignElementFactory.getInstance( )
				.newLabel( null );

		// dataHandle.setDisplayName( NAME );
		dataHandle.setText( LABEL_NAME );
		cellHandle.addContent( dataHandle );
	}

	private static final String DISPALY_NAME = Messages.getString( "AddSubTotalAction.TotalName" );//$NON-NLS-1$

	public static void CreateSubTotalLabel( LevelViewHandle levelView,
			CrosstabCellHandle cellHandle ) throws SemanticException
	{
		LabelHandle dataHandle = DesignElementFactory.getInstance( )
		.newLabel(null );
		//Label name is a compand name.
		//dataHandle.setText( "[" + levelView.getCubeLevelName( )+ "]" + DISPALY_NAME);
		dataHandle.setText( "[" + levelView.getCubeLevel( ).getName( )+ "]" + DISPALY_NAME);
		cellHandle.addContent( dataHandle );
	}

	public static ICubeQueryDefinition createBindingQuery(
			CrosstabReportItemHandle crosstabItem )
			throws Exception
	{
		ICubeQueryDefinition cubeQuery = CubeElementFactory.instance( )
				.createCubeQuery( crosstabItem.getCubeName( ) );

		List rowLevelNameList = new ArrayList( );
		List columnLevelNameList = new ArrayList( );

		// add row edge
		if ( crosstabItem.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE ) > 0 )
		{
			// TODO check visibility?
			IEdgeDefinition rowEdge = cubeQuery.createEdge( ICubeQueryDefinition.ROW_EDGE );

			for ( int i = 0; i < crosstabItem.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE ); i++ )
			{
				DimensionViewHandle dv = crosstabItem.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
						i );

				if ( dv.getCubeDimension( ) == null )
				{
					throw new CrosstabException( dv.getModelHandle( )
							.getElement( ),
							Messages.getString( "CrosstabQueryHelper.error.invalid.dimension" ) ); //$NON-NLS-1$
				}

				IDimensionDefinition dimDef = rowEdge.createDimension( dv.getCubeDimension( )
						.getName( ) );

				IHierarchyDefinition hieDef = dimDef.createHierarchy( dv.getCubeDimension( )
						.getDefaultHierarchy( )
						.getName( ) );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					LevelViewHandle lv = dv.getLevel( j );

					if ( lv.getCubeLevel( ) == null )
					{
						throw new CrosstabException( lv.getModelHandle( )
								.getElement( ),
								Messages.getString( "CrosstabQueryHelper.error.invalid.level" ) ); //$NON-NLS-1$
					}

					ILevelDefinition levelDef = hieDef.createLevel( lv.getCubeLevel( )
							.getName( ) );

					rowLevelNameList.add( lv.getCubeLevel( ).getFullName( ) );

				}
			}

		}

		// add column edge
		if ( crosstabItem.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ) > 0 )
		{
			// TODO check visibility?
			IEdgeDefinition columnEdge = cubeQuery.createEdge( ICubeQueryDefinition.COLUMN_EDGE );

			for ( int i = 0; i < crosstabItem.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ); i++ )
			{
				DimensionViewHandle dv = crosstabItem.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
						i );

				if ( dv.getCubeDimension( ) == null )
				{
					throw new CrosstabException( dv.getModelHandle( )
							.getElement( ),
							Messages.getString( "CrosstabQueryHelper.error.invalid.dimension" ) ); //$NON-NLS-1$
				}

				IDimensionDefinition dimDef = columnEdge.createDimension( dv.getCubeDimension( )
						.getName( ) );

				IHierarchyDefinition hieDef = dimDef.createHierarchy( dv.getCubeDimension( )
						.getDefaultHierarchy( )
						.getName( ) );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					LevelViewHandle lv = dv.getLevel( j );

					if ( lv.getCubeLevel( ) == null )
					{
						throw new CrosstabException( lv.getModelHandle( )
								.getElement( ),
								Messages.getString( "CrosstabQueryHelper.error.invalid.level" ) ); //$NON-NLS-1$
					}

					ILevelDefinition levelDef = hieDef.createLevel( lv.getCubeLevel( )
							.getName( ) );

					columnLevelNameList.add( lv.getCubeLevel( ).getFullName( ) );

				}
			}

		}

		// add column binding
		Iterator bindingItr = ( (ExtendedItemHandle) crosstabItem.getModelHandle( ) ).columnBindingsIterator( );
		ModuleHandle module = ( (ExtendedItemHandle) crosstabItem.getModelHandle( ) ).getModuleHandle( );

		if ( bindingItr != null )
		{
			Map cache = new HashMap( );

			while ( bindingItr.hasNext( ) )
			{
				ComputedColumnHandle column = (ComputedColumnHandle) bindingItr.next( );

				Binding binding = new Binding( column.getName( ) );
				binding.setAggrFunction( column.getAggregateFunction( ) == null ? null
						: DataAdapterUtil.adaptModelAggregationType( column.getAggregateFunction( ) ) );
				binding.setExpression( new ScriptExpression( column.getExpression( ) ) );

				List aggrList = column.getAggregateOnList( );

				if ( aggrList != null )
				{
					for ( Iterator aggrItr = aggrList.iterator( ); aggrItr.hasNext( ); )
					{
						String baseLevel = (String) aggrItr.next( );

						addHierachyAggregateOn( module,
								binding,
								baseLevel,
								rowLevelNameList,
								columnLevelNameList,
								cache );
					}
				}

				cubeQuery.addBinding( binding );
			}
		}

		return cubeQuery;

	}

	private static void addHierachyAggregateOn( ModuleHandle module,
			Binding binding, String baseLevel, List rowLevelList,
			List columnLevelList, Map cache ) throws BirtException
	{
		if ( binding == null || baseLevel == null || module == null )
		{
			return;
		}

		int sindex = rowLevelList.indexOf( baseLevel );

		if ( sindex != -1 )
		{
			for ( int i = 0; i <= sindex; i++ )
			{
				String levelName = (String) rowLevelList.get( i );
				String cachedExpression = (String) cache.get( levelName );

				if ( cachedExpression == null )
				{
					cachedExpression = createAggregateLevelExpression( module.findLevel( levelName ) );
					cache.put( levelName, cachedExpression );
				}

				if ( cachedExpression != null )
				{
					binding.addAggregateOn( cachedExpression );
				}
			}
		}

		if ( sindex != -1 )
		{
			// already found on row list, skip on column list
			return;
		}

		sindex = columnLevelList.indexOf( baseLevel );

		if ( sindex != -1 )
		{
			for ( int i = 0; i <= sindex; i++ )
			{
				String levelName = (String) columnLevelList.get( i );
				String cachedExpression = (String) cache.get( levelName );

				if ( cachedExpression == null )
				{
					cachedExpression = createAggregateLevelExpression( module.findLevel( levelName ) );
					cache.put( levelName, cachedExpression );
				}

				if ( cachedExpression != null )
				{
					binding.addAggregateOn( cachedExpression );
				}
			}
		}

	}

	private static String createAggregateLevelExpression( LevelHandle level )
	{
		if ( level != null && level.getContainer( ) != null )
		{
			DimensionHandle dimension = (DimensionHandle) level.getContainer( )
					.getContainer( );

			if ( dimension != null )
			{
				return ExpressionUtil.createJSDimensionExpression( dimension.getName( ),
						level.getName( ) );
			}
		}
		return null;
	}

	
}
