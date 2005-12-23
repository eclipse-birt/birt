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
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;

public class ActionImpl implements IAction
{

	private ActionHandle action;

	private ReportItemHandle handle;

	private void init( ActionHandle action, ReportItemHandle handle )
	{
		this.action = action;
		this.handle = handle;
	}

	public ActionImpl( ActionHandle action, LabelHandle handle )
	{
		init( action, handle );
	}

	public ActionImpl( ActionHandle action, ImageHandle handle )
	{
		init( action, handle );
	}

	public ActionImpl( ActionHandle action, DataItemHandle handle )
	{
		init( action, handle );
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
			checkAction( );
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
			checkAction( );
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
		checkAction( );
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
		checkAction( );
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
		checkAction( );
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
		checkAction( );
		try
		{
			action.setTargetBookmark( bookmark );
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	private void checkAction( ) throws ScriptException
	{
		if ( action != null )
			return;
		Action a = new Action( );
		try
		{
			if ( handle instanceof LabelHandle )
			{

				( ( LabelHandle ) handle ).setAction( a );
				action = ( ( LabelHandle ) handle ).getActionHandle( );
			} else if ( handle instanceof ImageHandle )
			{
				( ( ImageHandle ) handle ).setAction( a );
				action = ( ( ImageHandle ) handle ).getActionHandle( );
			} else if ( handle instanceof DataItemHandle )
			{
				( ( DataItemHandle ) handle ).setAction( a );
				action = ( ( DataItemHandle ) handle ).getActionHandle( );
			}
		} catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

}
