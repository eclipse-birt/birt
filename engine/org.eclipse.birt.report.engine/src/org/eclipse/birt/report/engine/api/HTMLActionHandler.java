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

package org.eclipse.birt.report.engine.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Defines a default action handler for HTML output format
 */
public class HTMLActionHandler implements IHTMLActionHandler {
	
	/** logger */
	protected Logger log = Logger.getLogger( HTMLActionHandler.class
			.getName( ) );
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IHTMLActionHandler#getURL(org.eclipse.birt.report.engine.api2.IAction, java.lang.Object)
	 */
	public String getURL(IAction actionDefn, Object context)
	{
		if ( actionDefn == null )
		{
			return null;
		}
		switch ( actionDefn.getType( ) )
		{
			case IAction.ACTION_BOOKMARK :
				return actionDefn.getActionString();
			case IAction.ACTION_HYPERLINK :
				return actionDefn.getActionString();

			case IAction.ACTION_DRILLTHROUGH :
				return buildDrillAction( actionDefn, context );

		}
		assert false;
		return null;
		
	}

	/**
	 * builds URL for drillthrough action
	 * 
	 * @param action instance of the IAction instance
	 * @param context the context for building the action string
	 * @return a URL 
	 */
	protected String buildDrillAction( IAction action, Object context )
	{
		String baseURL = null;
		if(context!=null && context instanceof HTMLRenderContext)
		{
			baseURL = ((HTMLRenderContext)context).getBaseURL();
		}
		StringBuffer link = new StringBuffer( );

		String reportName = action.getReportName( );
		if ( reportName != null && !reportName.equals( "" ) )//$NON-NLS-1$
		{

			link.append( baseURL );
			link.append( "?__report=" );	//$NON-NLS-1$
			try
			{
				link.append( URLEncoder.encode( reportName, "UTF-8" ) ); 	//$NON-NLS-1$
			}
			catch ( UnsupportedEncodingException e1 )
			{
				//It should not happen. Does nothing
			}
		

			//Adds the parameters
			if ( action.getParameterBindings( ) != null )
			{
				Iterator paramsIte = action.getParameterBindings( ).entrySet( ).iterator( );
				while ( paramsIte.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) paramsIte.next( );
					try
					{
						link.append( "&" + URLEncoder.encode( (String) entry.getKey( ), "UTF-8" ) + "=" + URLEncoder.encode( (String) entry.getValue( ), "UTF-8" ) );//$NON-NLS-1$				
					}
					catch ( UnsupportedEncodingException e )
					{
						//Does nothing
					}
				}
			}
		}

		//The search rules are not supported yet.
		if ( action.getBookmark( ) != null )
		{
			link.append( "#" );//$NON-NLS-1$
			link.append( action.getBookmark( ) );
		}

		return link.toString( );
	}

}
