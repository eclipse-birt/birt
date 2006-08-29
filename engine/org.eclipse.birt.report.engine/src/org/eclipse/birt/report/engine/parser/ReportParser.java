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

package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

import com.ibm.icu.util.ULocale;

/**
 * Report Parser.
 * 
 * used to parse the design file, and get the IR of design.
 * 
 * 
 * @version $Revision: 1.17 $ $Date: 2006/08/22 05:55:28 $
 */
public class ReportParser
{

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger( ReportParser.class
			.getName( ) );

	private Map options = new HashMap( );

	/**
	 * constructor.
	 */
	public ReportParser( )
	{
	}

	public ReportParser( IReportEngine engine )
	{
		loadOption( engine );
	}

	public ReportParser( ExecutionContext context )
	{

		if ( context != null )
		{
			IReportEngine engine = context.getEngine( );
			if ( engine != null )
			{
				loadOption( engine );
			}
		}
	}
	
	protected void loadOption( IReportEngine engine )
	{
		if ( engine != null )

		{
			EngineConfig config = engine.getConfig( );
			if ( config != null )
			{
				Object locator = config.getResourceLocator( );
				if ( locator != null )
				{
					options.put( ModuleOption.RESOURCE_LOCATOR_KEY, locator );
				}
				Object resourcePath = config.getResourcePath( );
				if ( resourcePath != null )
				{
					options
							.put( ModuleOption.RESOURCE_FOLDER_KEY,
									resourcePath );
				}
			}
		}
	}

	public ReportParser( Map options )
	{
		this.options.putAll( options );
	}

	/**
	 * parse the XML input stream.
	 * 
	 * @param name
	 *            design file name
	 * 
	 * @param in
	 *            design file
	 * @return created report IR, null if exit any errors.
	 */
	public Report parse( String name, InputStream in )
			throws DesignFileException
	{
		ReportDesignHandle designHandle = getDesignHandle( name, in );

		return parse( designHandle );
	}
	
	/**
	 * parse the XML input stream.
	 * 
	 * @param name
	 *            design file name
	 * @return created report IR, null if exit any errors.
	 */
	public Report parse( String name ) throws DesignFileException
	{
		ReportDesignHandle designHandle = getDesignHandle( name, null );

		return parse( designHandle );
	}

	public ReportDesignHandle getDesignHandle( String name, InputStream in )
			throws DesignFileException
	{
		// Create new design session
		SessionHandle sessionHandle = DesignEngine.newSession( ULocale
				.getDefault( ) );

		// get the resource locator form the options and set it to the session
		// handle
		IResourceLocator locator = (IResourceLocator) options
				.get( ModuleOption.RESOURCE_LOCATOR_KEY );
		if ( locator != null )
		{
			sessionHandle.setResourceLocator( locator );
		}

		// Obtain design handle
		ReportDesignHandle designHandle = null;
		ModuleOption modOptions = new ModuleOption( options );
		if ( in != null )
			designHandle = sessionHandle.openDesign( name, in, modOptions );
		else
			designHandle = sessionHandle.openDesign( name, modOptions );

		return designHandle;
	}

	/**
	 * parse the XML input stream.
	 * 
	 * @param design
	 *            DE's IR
	 * @return FPE's IR, null if there is any error.
	 */
	public Report parse( ReportDesignHandle design )
	{
		assert ( design != null );
		// assert ( design.getErrorList().isEmpty());

		EngineIRVisitor visitor = new EngineIRVisitor( design );
		return visitor.translate( );
	}
	
	/**
	 * Gets the parameter list of the report.
	 * 
	 * @param design -
	 *            the handle of the report design
	 * @param includeParameterGroups
	 *            A <code>boolean</code> value specifies whether to include
	 *            parameter groups or not.
	 * @return The collection of top-level report parameters and parameter
	 *         groups if <code>includeParameterGroups</code> is set to
	 *         <code>true</code>; otherwise, returns all the report
	 *         parameters.
	 */
	public ArrayList getParameters( ReportDesignHandle design,
			boolean includeParameterGroups )
	{
		assert ( design != null );
		EngineIRVisitor visitor = new EngineIRVisitor( design );
		ArrayList parameters = new ArrayList( );

		SlotHandle paramSlot = design.getParameters( );
		IParameterDefnBase param;
		for ( int i = 0; i < paramSlot.getCount( ); i++ )
		{
			visitor.apply( paramSlot.get( i ) );
			assert ( visitor.currentElement != null );
			param = (IParameterDefnBase) visitor.currentElement;
			assert ( param.getName( ) != null );
			parameters.add( param );
		}
		
		if ( includeParameterGroups )
			return parameters;
		else
			return flattenParameter( parameters );
	}
	
	/**
	 * Puts all the report parameters including those appear inside parameter
	 * groups to the <code>allParameters</code> object.
	 * 
	 * @param params
	 *            A collection of parameters and parameter groups.
	 */
	protected ArrayList flattenParameter( ArrayList params )
	{
		assert params != null;
		IParameterDefnBase param;
		ArrayList allParameters = new ArrayList( );

		for ( int n = 0; n < params.size( ); n++ )
		{
			param = (IParameterDefnBase) params.get( n );
			if ( param.getParameterType( ) == IParameterDefnBase.PARAMETER_GROUP
					|| param.getParameterType( ) == IParameterDefnBase.CASCADING_PARAMETER_GROUP )
			{
				allParameters
						.addAll( flattenParameter( ( (IParameterGroupDefn) param )
								.getContents( ) ) );
			}
			else
			{
				allParameters.add( param );
			}
		}
		
		return allParameters;
	}
	
}