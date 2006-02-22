/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import java.io.Serializable;
import com.ibm.icu.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * Implementation of the IReportContext interface
 */
public class ReportContextImpl implements IReportContext
{

	private ExecutionContext context;

	public ReportContextImpl( ExecutionContext context )
	{
		this.context = context;
	}

	public Map getAppContext( )
	{
		return context.getAppContext( );
	}

	public void setAppContext( Map appContext )
	{
		context.setAppContext( appContext );
	}

	public void setGlobalVariable( String name, Object obj )
	{
		context.registerBean( name, obj );
	}

	public void deleteGlobalVariable( String name )
	{
		context.registerBean( name, null );
	}

	public Object getGlobalVariable( String name )
	{
		return context.getBeans( ).get( name );
	}

	public void setPersistentGlobalVariable( String name, Serializable obj )
	{
		context.registerGlobalBean( name, obj );
	}

	public void deletePersistentGlobalVariable( String name )
	{
		context.unregisterGlobalBean( name );
	}

	public Object getPersistentGlobalVariable( String name )
	{
		return context.getGlobalBeans( ).get( name );
	}

	public void setRegisteredPersistantObjects( Map persistantMap )
	{
		context.registerBeans( persistantMap );
	}

	public Object getParameterValue( String name )
	{
		return context.getParams( ).get( name );
	}

	public void setParameterValue( String name, Object value )
	{
		context.setParamter( name, value );
	}

	public Object getConfigVariableValue( String varName )
	{
		return context.getConfigs( ).get( varName );
	}

	public Locale getLocale( )
	{
		return context.getLocale( );
	}

	public String getOutputFormat( )
	{
		return context.getOutputFormat( );
	}

	public Object getHttpServletRequest( )
	{
		return getAppContext( ).get( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST );
	}

	public String getMessage( String key )
	{
		return context.getDesign( ).getMessage( key );
	}

	public String getMessage( String key, Locale locale )
	{
		return context.getDesign( ).getMessage( key, locale );
	}

	public String getMessage( String key, Object[] params )
	{
		String msg = context.getDesign( ).getMessage( key );
		if ( msg == null )
			return "";
		return MessageFormat.format( msg, params );
	}

	public String getMessage( String key, Locale locale, Object[] params )
	{
		String msg = context.getDesign( ).getMessage( key, locale );
		if ( msg == null )
			return "";
		return MessageFormat.format( msg, params );
	}
}
