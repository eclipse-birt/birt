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

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;

public class ActionImpl extends Structure implements IAction
{

	private ReportItemHandle handle;

	public ActionImpl( ActionHandle action, ReportItemHandle handle )
	{
		super( action );
		this.handle = handle;
	}

	public String getURI( )
	{
		return ( (ActionHandle) structureHandle ).getURI( );
	}

	public String getTargetWindow( )
	{
		return ( (ActionHandle) structureHandle ).getTargetWindow( );
	}

	public String getLinkType( )
	{
		return ( (ActionHandle) structureHandle ).getLinkType( );
	}

	public void setLinkType( String type ) throws SemanticException
	{
		checkAction( );
		ActivityStack cmdStack = handle.getModule( ).getActivityStack( );

		cmdStack.startNonUndoableTrans( null );
		try
		{
			( (ActionHandle) structureHandle ).setLinkType( type );
		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
	}

	public void setFormatType( String type ) throws SemanticException
	{

		checkAction( );
		setProperty( Action.FORMAT_TYPE_MEMBER, type );

	}

	public String getFormatType( )
	{
		return ( (ActionHandle) structureHandle ).getFormatType( );
	}

	public void setTargetWindow( String window ) throws SemanticException
	{
		checkAction( );

		setProperty( Action.TARGET_WINDOW_MEMBER, window );
	}

	public void setURI( String uri ) throws SemanticException
	{
		checkAction( );

		setProperty( Action.URI_MEMBER, uri );

	}

	public String getReportName( )
	{
		return ( (ActionHandle) structureHandle ).getReportName( );
	}

	public void setReportName( String reportName ) throws SemanticException
	{
		checkAction( );
		setProperty( Action.REPORT_NAME_MEMBER, reportName );
	}

	public String getTargetBookmark( )
	{
		return ( (ActionHandle) structureHandle ).getTargetBookmark( );
	}

	public void setTargetBookmark( String bookmark ) throws SemanticException
	{
		checkAction( );

		setProperty( Action.TARGET_BOOKMARK_MEMBER, bookmark );

	}

	private void checkAction( ) throws SemanticException
	{
		if ( structureHandle != null )
			return;
		Action a = new Action( );

		if ( handle instanceof LabelHandle )
		{
			( (LabelHandle) handle ).setAction( a );
			structureHandle = ( (LabelHandle) handle ).getActionHandle( );
		}
		else if ( handle instanceof ImageHandle )
		{
			( (ImageHandle) handle ).setAction( a );
			structureHandle = ( (ImageHandle) handle ).getActionHandle( );
		}
		else if ( handle instanceof DataItemHandle )
		{
			( (DataItemHandle) handle ).setAction( a );
			structureHandle = ( (DataItemHandle) handle ).getActionHandle( );
		}
		else
		{
			throw new PropertyNameException( handle == null ? null : handle
					.getElement( ), ILabelModel.ACTION_PROP );
		}
	}

}
