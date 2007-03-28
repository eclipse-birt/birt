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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.simpleapi.IAction;

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

	public void setLinkType( String type ) throws SemanticException
	{
		checkAction( );
		action.setLinkType( type );

	}

	public void setFormatType( String type ) throws SemanticException
	{

		checkAction( );
		action.setFormatType( type );

	}

	public String getFormatType( )
	{
		return action.getFormatType( );
	}

	public void setTargetWindow( String window ) throws SemanticException
	{
		checkAction( );

		action.setTargetWindow( window );
	}

	public void setURI( String uri ) throws SemanticException
	{
		checkAction( );

		action.setURI( uri );

	}

	public String getReportName( )
	{
		return action.getReportName( );
	}

	public void setReportName( String reportName ) throws SemanticException
	{
		checkAction( );
		action.setReportName( reportName );
	}

	public String getTargetBookmark( )
	{
		return action.getTargetBookmark( );
	}

	public void setTargetBookmark( String bookmark ) throws SemanticException
	{
		checkAction( );

		action.setTargetBookmark( bookmark );

	}

	private void checkAction( ) throws SemanticException
	{
		if ( action != null )
			return;
		Action a = new Action( );

		if ( handle instanceof LabelHandle )
		{

			( (LabelHandle) handle ).setAction( a );
			action = ( (LabelHandle) handle ).getActionHandle( );
		}
		else if ( handle instanceof ImageHandle )
		{
			( (ImageHandle) handle ).setAction( a );
			action = ( (ImageHandle) handle ).getActionHandle( );
		}
		else if ( handle instanceof DataItemHandle )
		{
			( (DataItemHandle) handle ).setAction( a );
			action = ( (DataItemHandle) handle ).getActionHandle( );
		}
	}

}
