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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportResourceEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * The action used to add library to a report design
 */

public class AddSelectedLibToCurrentReportDesignAction extends Action
{

	private StructuredViewer viewer;

	private static final String ACTION_TEXT = Messages.getString( "UseLibraryAction.Text" ); //$NON-NLS-1$

	public AddSelectedLibToCurrentReportDesignAction( StructuredViewer viewer )
	{
		super( ACTION_TEXT );
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		LibraryHandle library = getSelectedLibrary( );
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		boolean enabled = library != null
				&& moduleHandle != null
				&& !moduleHandle.isInclude( library )
				&& ( library.getFileName( ) != null && !library.getFileName( )
						.equals( moduleHandle.getFileName( ) ) );
		if ( library != null )
			library.close( );

		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		if ( isEnabled( ) )
		{
			LibraryHandle library = getSelectedLibrary( );
			try
			{
				UIUtil.includeLibrary( library );
			}
			catch ( Exception e )
			{
				ExceptionUtil.handle( e );
			}
		}
	}

	/*
	 * Need model to provide a new api to support it.
	 */
	@Deprecated
	private boolean testRun( LibraryHandle library )
	{
		boolean enabled = false;
		CommandStack commandStack = Utility.getCommandStack( );
		commandStack.startTrans( "" );
		try
		{
			// we set the flag "isDefault" to true here to mimic the behavior
			// that the namespace cannot be changed, so renaming UI will not be
			// triggered during this testing mode.
			UIUtil.includeLibrary( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ), library, true );
			enabled = true;
		}
		catch ( Exception e )
		{
			enabled = false;
		}
		commandStack.rollback( );
		return enabled;
	}

	private LibraryHandle getSelectedLibrary( )
	{
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection( );
		if ( selection != null && selection.size( ) == 1 )
		{
			Object selected = selection.getFirstElement( );

			if ( selected instanceof LibraryHandle )
			{
				return (LibraryHandle) selected;
			}
			else if ( selected instanceof ReportResourceEntry
					&& ( (ReportResourceEntry) selected ).getReportElement( ) instanceof LibraryHandle )
			{
				return (LibraryHandle) ( (ReportResourceEntry) selected ).getReportElement( );
			}
			else if ( selected instanceof ResourceEntryWrapper
					&& ( (ResourceEntryWrapper) selected ).getType( ) == ResourceEntryWrapper.LIBRARY )
			{
				return (LibraryHandle) ( (ResourceEntryWrapper) selected ).getAdapter( LibraryHandle.class );
			}
		}
		return null;
	}

}
