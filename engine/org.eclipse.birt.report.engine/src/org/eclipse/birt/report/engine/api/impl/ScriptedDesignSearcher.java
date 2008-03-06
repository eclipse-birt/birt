/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;

public class ScriptedDesignSearcher extends ScriptedDesignVisitor
{

	protected boolean hasOnPrepareScript = false;

	ScriptedDesignSearcher( ReportDesignHandle handle )
	{
		super( handle );
	}

	public void apply( DesignElementHandle handle )
	{
		try
		{
			super.apply( handle );
		}
		catch ( StopException e )
		{
			hasOnPrepareScript = true;
		}
	}

	public boolean hasOnPrepareScript( )
	{
		return hasOnPrepareScript;
	}

	protected void handleOnPrepare( ReportItemHandle handle )
	{
		boolean hasJavaScript = ( handle.getOnPrepare( ) != null )
				&& ( handle.getOnPrepare( ).length( ) != 0 );
		boolean hasJavaCode = ( handle.getEventHandlerClass( ) != null )
				&& ( handle.getEventHandlerClass( ).length( ) != 0 );
		if ( hasJavaScript || hasJavaCode )
		{
			throw new StopException( );
		}

	}

	protected void handleOnPrepare( CellHandle handle )
	{
		boolean hasJavaScript = ( handle.getOnPrepare( ) != null )
				&& ( handle.getOnPrepare( ).length( ) != 0 );
		boolean hasJavaCode = ( handle.getEventHandlerClass( ) != null )
				&& ( handle.getEventHandlerClass( ).length( ) != 0 );
		if ( hasJavaScript || hasJavaCode )
		{
			throw new StopException( );
		}

	}

	protected void handleOnPrepare( GroupHandle handle )
	{
		boolean hasJavaScript = ( handle.getOnPrepare( ) != null )
				&& ( handle.getOnPrepare( ).length( ) != 0 );
		boolean hasJavaCode = ( handle.getEventHandlerClass( ) != null )
				&& ( handle.getEventHandlerClass( ).length( ) != 0 );
		if ( hasJavaScript || hasJavaCode )
		{
			throw new StopException( );
		}

	}

	protected void handleOnPrepare( RowHandle handle )
	{
		boolean hasJavaScript = ( handle.getOnPrepare( ) != null )
				&& ( handle.getOnPrepare( ).length( ) != 0 );
		boolean hasJavaCode = ( handle.getEventHandlerClass( ) != null )
				&& ( handle.getEventHandlerClass( ).length( ) != 0 );
		if ( hasJavaScript || hasJavaCode )
		{
			throw new StopException( );
		}

	}

	class StopException extends RuntimeException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1793414995245120248L;

	}

}
