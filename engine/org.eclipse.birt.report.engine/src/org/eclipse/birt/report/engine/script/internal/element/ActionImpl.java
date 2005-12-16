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
package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class ActionImpl implements IAction
{

	private ActionHandle action;

	public ActionImpl( ActionHandle action )
	{
		this.action = action;
	}

	public String getURI( )
	{
		return action.getURI( );
	}

	public String getTargetWindow( )
	{
		return action.getTargetWindow( );
	}

	public String getLinkType( )
	{
		return action.getLinkType( );
	}

	public void setLinkType( String type ) throws ScriptException
	{
		try
		{
			action.setLinkType( type );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	public void setFormatType( String type ) throws ScriptException
	{
		try
		{
			action.setFormatType( type );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	public String getFormatType( )
	{
		return action.getFormatType( );
	}

	public void setTargetWindow( String window ) throws ScriptException
	{
		try
		{
			action.setTargetWindow( window );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	public void setURI( String uri ) throws ScriptException
	{
		try
		{
			action.setURI( uri );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getReportName( )
	{
		return action.getReportName( );
	}

	public void setReportName( String reportName ) throws ScriptException
	{
		try
		{
			action.setReportName( reportName );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public String getTargetBookmark( )
	{
		return action.getTargetBookmark( );
	}

	public void setTargetBookmark( String bookmark ) throws ScriptException
	{
		try
		{
			action.setTargetBookmark( bookmark );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

}
