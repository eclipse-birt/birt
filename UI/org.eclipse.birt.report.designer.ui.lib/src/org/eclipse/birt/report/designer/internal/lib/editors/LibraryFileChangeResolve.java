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

package org.eclipse.birt.report.designer.internal.lib.editors;

import org.eclipse.birt.report.designer.internal.ui.editors.IRelatedFileChangeResolve;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.LibrarySaveChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 */

public class LibraryFileChangeResolve implements IRelatedFileChangeResolve
{

	public boolean acceptType( int type )
	{
		return type == IReportResourceChangeEvent.LibraySaveChange;
	}

	public boolean isReload( IReportResourceChangeEvent event,
			ModuleHandle owner )
	{
		return ModuleUtil.isInclude( owner,
				( (LibrarySaveChangeEvent) event ).getFileName( ) );
	}

	public boolean isReset( IReportResourceChangeEvent event, ModuleHandle owner )
	{
		return owner.getFileName( )
				.equals( ( (LibrarySaveChangeEvent) event ).getFileName( ) );
	}

	public void notifySaveFile( ModuleHandle owner )
	{
		if ( owner instanceof LibraryHandle )
		{
			IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault( )
					.getResourceSynchronizerService( );

			if ( synchronizer != null )
			{
				synchronizer.notifyResourceChanged( new LibrarySaveChangeEvent( owner,
						null,
						IReportResourceChangeEvent.LibraySaveChange,
						owner.getFileName( ) ) );
			}
		}

	}

	public boolean reload( ModuleHandle owner )
	{
		if ( MessageDialog.openConfirm( UIUtil.getDefaultShell( ),
				Messages.getString( "MultiPageReportEditor.ConfirmVersion.Dialog.Title" ), Messages.getString( "MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage" ) ) ) //$NON-NLS-1$ //$NON-NLS-2$ 
		{
			UIUtil.reloadModuleHandleLibraries( owner );
			return true;
		}
		return false;
	}

	public boolean reset(  )
	{
		if ( MessageDialog.openConfirm( UIUtil.getDefaultShell( ),
				Messages.getString( "MultiPageReportEditor.ConfirmVersion.Dialog.Title" ), Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage") ) ) //$NON-NLS-1$ //$NON-NLS-2$ 
		{
			return true;
		}
		return false;
	}
}
