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

import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Report is the root element of the design.
 * 
 * @version $Revision: 1.15 $ $Date: 2005/05/20 03:39:29 $
 */
public class Report
{

	/**
	 * A collection that stores all the report parameters.
	 */
	protected ArrayList allParameters = null;

	/**
	 * report design get from Model
	 */
	protected ReportDesign reportDesign;

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
	 * A collection that stores the top level report parameters and parameter
	 * groups.
	 */
	protected ArrayList parameters = new ArrayList( );

	/**
	 * queries used by this report.
	 * 
	 * @see org.eclipse.birt.report.engine.anlyzer.IQueryDefinition
	 */
	protected ArrayList queries = new ArrayList( );

	/**
	 * Page setup this report used
	 */
	protected PageSetupDesign pageSetup = new PageSetupDesign( );

	/**
	 * Report body
	 */
	protected ArrayList contents = new ArrayList( );

	/**
	 * The base directory of the relative links. By default it is where design
	 * file (XML) resides
	 */
	protected String basePath;

	/**
	 * the non-inheritable report style
	 */
	protected StyleDesign nonInheritableStyle;
	
	/**
	 * the inheritable report style
	 */
	protected StyleDesign inheritableStyle;

	/**
	 * default constructor.
	 */
	public Report( )
	{
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
	public void addStyle( StyleDesign style )
	{
		assert ( style != null );
		assert ( style.getName( ) != null );
		this.styles.add( style );
		this.styleTable.put( style.getName( ), style );
	}

	/**
	 * Finds the style in the report.
	 * 
	 * @param name
	 *            The name of the style.
	 * @return The corresponding <code>StyleDesign</code> object.
	 */
	public StyleDesign findStyle( String name )
	{
		assert ( name != null );
		return (StyleDesign) this.styleTable.get( name );
	}

	/**
	 * Appends a top-level parameter or parameter group in the report.
	 * 
	 * @param parameter
	 *            The parameter or parameter group object.
	 */
	public void addParameter( IParameterDefnBase parameter )
	{
		assert ( parameter != null );
		assert ( parameter.getName( ) != null );
		this.parameters.add( parameter );
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
	public ReportDesign getReportDesign( )
	{
		return reportDesign;
	}

	/**
	 * @param reportDesign
	 *            The reportDesign to set.
	 */
	public void setReportDesign( ReportDesign reportDesign )
	{
		this.reportDesign = reportDesign;
		if ( basePath == null || basePath.equals( "" ) ) //$NON-NLS-1$
		{
			File file = new File( reportDesign.getFileName( ) );
			basePath = file.getParent( );
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

	/**
	 * Puts all the report parameters including those appear inside parameter
	 * groups to the <code>allParameters</code> object.
	 * 
	 * @param params
	 *            A collection of parameters and parameter groups.
	 */
	protected void flattenParameter( ArrayList params )
	{
		assert allParameters != null;
		assert params != null;
		IParameterDefnBase param;
		for ( int n = 0; n < params.size( ); n++ )
		{
			param = (IParameterDefnBase) params.get( n );
			if ( param.getParameterType() == IParameterDefnBase.PARAMETER_GROUP )
			{
				flattenParameter( ( (IParameterGroupDefn) param )
						.getContents() );
			}
			else
			{
				allParameters.add( param );
			}
		}
	}

	/**
	 * Gets the parameter list of the report.
	 * 
	 * @param includeParameterGroups
	 *            A <code>boolean</code> value specifies whether to include
	 *            parameter groups or not.
	 * @return The collection of top-level report parameters and parameter
	 *         groups if <code>includeParameterGroups</code> is set to
	 *         <code>true</code>; otherwise, returns all the report
	 *         parameters.
	 */
	public ArrayList getParameters( boolean includeParameterGroups )
	{
		if ( includeParameterGroups )
		{
			return parameters;
		}

		if ( allParameters != null )
		{
			return allParameters;
		}

		allParameters = new ArrayList( );
		flattenParameter( parameters );
		return allParameters;
	}

	public HashMap getConfigs( )
	{
		HashMap configs = new HashMap( );
		Iterator iter = reportDesign.handle( ).configVariablesIterator( );
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

	public String getAfterCloseDoc( )
	{
		return reportDesign.handle( ).getAfterCloseDoc( );
	}

	public String getAfterFactory( )
	{
		return reportDesign.handle( ).getAfterFactory( );
	}

	public String getAfterOpenDoc( )
	{
		return reportDesign.handle( ).getAfterOpenDoc( );
	}

	public String getAfterRender( )
	{
		return reportDesign.handle( ).getAfterRender( );
	}

	public String getBeforeCloseDoc( )
	{
		return reportDesign.handle( ).getBeforeCloseDoc( );
	}

	public String getBeforeFactory( )
	{
		return reportDesign.handle( ).getBeforeFactory( );
	}

	public String getBeforeOpenDoc( )
	{
		return reportDesign.handle( ).getBeforeOpenDoc( );
	}

	public String getBeforeRender( )
	{
		return reportDesign.handle( ).getBeforeRender( );
	}

	public List getIncludeScripts( )
	{
		ArrayList includes = new ArrayList( );
		Iterator iter = reportDesign.handle( ).includeScriptsIterator( );
		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				IncludeScriptHandle handle = (IncludeScriptHandle) iter.next( );
				includes.add( handle.getFileName( ) );
			}
		}
		return includes;
	}
	
	public String getInitialize()
	{
		return reportDesign.handle().getInitialize();
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

	public List getErrors( )
	{
		return this.reportDesign.handle( ).getErrorList( );
	}

	/**
	 * @return Returns the defaultStyle.
	 */
	public IStyle getNonInheritableStyle( )
	{
		return nonInheritableStyle;
	}

	/**
	 * @return Returns the defaultStyle.
	 */
	public IStyle getInheritableStyle( )
	{
		return inheritableStyle;
	}

	/**
	 * @param defaultStyle
	 *            The bodyStyle to set.
	 */
	public void setDefaultStyles( StyleDesign nonInheritableStyle, StyleDesign inheritableStyle )
	{
		this.nonInheritableStyle = nonInheritableStyle;
		this.inheritableStyle = inheritableStyle;
	}
}