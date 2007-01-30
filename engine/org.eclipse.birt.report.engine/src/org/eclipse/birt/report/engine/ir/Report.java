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

package org.eclipse.birt.report.engine.ir;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.css.CSSStyleDeclaration;

/**
 * Report is the root element of the design.
 * 
 */
public class Report
{

	/**
	 * report design get from Model
	 */
	protected ReportDesignHandle reportDesign;

	/**
	 * default unit
	 */
	protected String unit;

	/**
	 * styles used in this report
	 */
	protected ArrayList styles = new ArrayList( );

	/**
	 * style-name mapping table
	 */
	protected HashMap styleTable = new HashMap( );

	/**
	 * the name of Report root style
	 */
	protected String rootStyleName;

	/**
	 * queries used by this report.
	 * 
	 * @see org.eclipse.birt.report.engine.anlyzer.IQueryDefinition
	 */
	protected ArrayList queries = new ArrayList( );

	/**
	 * use to find the query IDs.(query, string) pair.
	 */
	protected HashMap queryIDs = new HashMap( );
	
	/**
	 * use to find the result MetaData.(query, ResultMetaData) pair.
	 */
	protected HashMap resultMetaData = new HashMap( );

	/**
	 * Page setup this report used
	 */
	protected PageSetupDesign pageSetup = new PageSetupDesign( );

	/**
	 * Report body
	 */
	protected ArrayList contents = new ArrayList( );

	protected Map namedExpressions;

	protected Map mapReportItemIDtoInstance;

	/**
	 * The base directory of the relative links. By default it is where design
	 * file (XML) resides
	 */
	protected String basePath;

	/**
	 * The prefix of style name
	 */
	public static final String PREFIX_STYLE_NAME = "style_"; //$NON-NLS-1$

	/*
	 * map report item to query
	 */
	protected HashMap mapReportItemToQuery;

	/*
	 * map query to "value" expressions
	 */
	protected HashMap mapQueryToValueExprs;
	
	protected HashMap mapValueExprToName;

	/**
	 * css engine used in this
	 */
	protected CSSEngine cssEngine;

	/**
	 * default constructor.
	 */
	public Report( )
	{
		cssEngine = new BIRTCSSEngine( );
	}

	public CSSEngine getCSSEngine( )
	{
		return cssEngine;
	}

	/**
	 * return the map from report item to query
	 * 
	 * @return the map from report item to query
	 */
	public HashMap getReportItemToQueryMap( )
	{
		if ( mapReportItemToQuery == null )
		{
			mapReportItemToQuery = new HashMap( );
		}
		return mapReportItemToQuery;
	}

	/**
	 * return the map from query to value expressions
	 * @deprecated 
	 * @return the map from query to value expressions;
	 */
	public HashMap getQueryToValueExprMap( )
	{
		if ( mapQueryToValueExprs == null )
		{
			mapQueryToValueExprs = new HashMap( );
		}
		return mapQueryToValueExprs;
	}
	
	public HashMap getExprToNameMap( )
	{
		if( this.mapValueExprToName == null )
		{
			this.mapValueExprToName = new HashMap( );
		}
		return this.mapQueryToValueExprs;
	}

	/**
	 * set report item id to report item instance
	 * 
	 * @param id
	 *            the report item component id
	 * @param rptItem
	 *            the report item
	 */
	public void setReportItemInstanceID( long id, ReportElementDesign rptElement )
	{
		if ( mapReportItemIDtoInstance == null )
		{
			mapReportItemIDtoInstance = new HashMap( );
		}
		mapReportItemIDtoInstance.put( new Long( id ), rptElement );
	}

	/**
	 * return the report item with the specific component ID
	 * 
	 * @param id
	 *            the component id
	 * @return the report item instance
	 */
	public ReportElementDesign getReportItemByID( long id )
	{
		assert mapReportItemIDtoInstance != null;
		return (ReportElementDesign) mapReportItemIDtoInstance.get( new Long(
				id ) );
	}

	/**
	 * return the named expression defined on the report
	 * 
	 * @return
	 */
	public Map getNamedExpressions( )
	{
		if ( namedExpressions == null )
			namedExpressions = new HashMap( );

		return namedExpressions;
	}

	/**
	 * set the report's page setup
	 * 
	 * @param pageSetup
	 *            page setup
	 */
	public void setPageSetup( PageSetupDesign pageSetup )
	{
		this.pageSetup = pageSetup;
	}

	/**
	 * get the report's page setup
	 * 
	 * @return page setup of this report
	 */
	public PageSetupDesign getPageSetup( )
	{
		return this.pageSetup;
	}

	/**
	 * get total contents.
	 * 
	 * @return sections in the report body.
	 */
	public ArrayList getContents( )
	{
		return this.contents;
	}

	/**
	 * get contents count in a report.
	 * 
	 * @return content count
	 */
	public int getContentCount( )
	{
		return this.contents.size( );
	}

	/**
	 * get content at index.
	 * 
	 * @param index
	 *            content index
	 * @return content
	 */
	public ReportItemDesign getContent( int index )
	{
		assert ( index >= 0 && index < this.contents.size( ) );
		return (ReportItemDesign) this.contents.get( index );
	}

	/**
	 * add content in to report body.
	 * 
	 * @param item
	 *            content to be added.
	 */
	public void addContent( ReportItemDesign item )
	{
		this.contents.add( item );
	}

	/**
	 * get number of shared styles defined in this report.
	 * 
	 * @return style number
	 */
	public int getStyleCount( )
	{
		return this.styles.size( );
	}

	/**
	 * get the style.
	 * 
	 * @param index
	 *            style index
	 * @return style
	 */
	public IStyle getStyle( int index )
	{
		assert ( index >= 0 && index < styles.size( ) );
		return (IStyle) this.styles.get( index );
	}

	/**
	 * add a style definition into the report.
	 * 
	 * @param style
	 *            style definition.
	 */
	public void addStyle( String name, CSSStyleDeclaration style )
	{
		assert ( style != null );
		this.styles.add( style );
		this.styleTable.put( name, style );
	}

	public Set getStyleSet( )
	{
		return styleTable.entrySet( );
	}

	/**
	 * Finds the style in the report.
	 * 
	 * @param name
	 *            The name of the style.
	 * @return The corresponding <code>StyleDesign</code> object.
	 */
	public IStyle findStyle( String name )
	{
		if ( name == null )
		{
			return null;
		}
		return (IStyle) this.styleTable.get( name );
	}

	/**
	 * Finds a master page with given name.
	 * 
	 * @param name
	 *            The name of the master page to locate.
	 * @return A <code>MasterPageDesign</code> object that describes the
	 *         master page, or <code>null</code> if no master page of the
	 *         given name is found.
	 */
	public MasterPageDesign findMasterPage( String name )
	{
		assert ( name != null );
		return this.pageSetup.findMasterPage( name );
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit( )
	{
		return unit;
	}

	/**
	 * @param unit
	 *            The unit to set.
	 */
	public void setUnit( String unit )
	{
		this.unit = unit;
	}

	/**
	 * get message of the resource key
	 * 
	 * @param resourceKey
	 *            resource key
	 * @param locale
	 *            locale.
	 * @return message text.
	 */
	public String getMessage( String resourceKey, Locale locale )
	{
		if ( this.reportDesign != null )
		{
			return this.reportDesign.getMessage( resourceKey, locale );
		}
		return null;
	}

	/**
	 * @return Returns the reportDesign.
	 */
	public ReportDesignHandle getReportDesign( )
	{
		return reportDesign;
	}

	/**
	 * @param reportDesign
	 *            The reportDesign to set.
	 */
	public void setReportDesign( ReportDesignHandle reportDesign )
	{
		this.reportDesign = reportDesign;
		if ( basePath == null || basePath.equals( "" ) ) //$NON-NLS-1$
		{
			String fileName = reportDesign.getFileName( );
			if ( fileName != null )
			{
				File file = new File( fileName );
				basePath = file.getParent( );
			}
		}
	}

	/**
	 * get queries used in this report.
	 * 
	 * @see org.eclipse.birt.report.engine.analysis.IReportQuery
	 * @return the list of the query
	 */
	public ArrayList getQueries( )
	{
		return this.queries;
	}

	public HashMap getQueryIDs( )
	{
		return this.queryIDs;
	}
	
	public HashMap getResultMetaData( )
	{
		return this.resultMetaData;
	}

	public HashMap getConfigs( )
	{
		HashMap configs = new HashMap( );
		Iterator iter = reportDesign.configVariablesIterator( );
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				ConfigVariableHandle handle = (ConfigVariableHandle) iter
						.next( );
				String name = handle.getName( );
				String value = handle.getValue( );
				configs.put( name, value );
			}
		}
		return configs;
	}

	/**
	 * Gets the directory where design file resides.
	 * 
	 * @return path
	 */
	public String getBasePath( )
	{
		return basePath;
	}

	/**
	 * @param basePath
	 *            The basePath to set.
	 */
	public void setBasePath( String basePath )
	{
		this.basePath = basePath;
	}

	/**
	 * the name of Report root style
	 */
	public String getRootStyleName( )
	{
		return rootStyleName;
	}

	public void setRootStyleName( String rootStyleName )
	{
		this.rootStyleName = rootStyleName;
	}

	public List getErrors( )
	{
		return this.reportDesign.getErrorList( );
	}
}
