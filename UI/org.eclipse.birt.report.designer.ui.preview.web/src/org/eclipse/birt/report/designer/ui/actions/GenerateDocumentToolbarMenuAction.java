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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * 
 */

public class GenerateDocumentToolbarMenuAction implements IWorkbenchWindowActionDelegate
{

	public void dispose( )
	{
	}

	public void init( IWorkbenchWindow window )
	{
	}

	public void run( IAction action )
	{
		gendoc( );
	}

	public void selectionChanged( IAction action, ISelection selection )
	{
	}

	private void gendoc( )
	{
		FormEditor editor = UIUtil.getActiveReportEditor( false );
		ModuleHandle model = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		if ( editor != null )
		{
			if ( model.needsSave( ) )
			{
				editor.doSave( null );
			}
		}

		Map options = new HashMap( );
		options.put( WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault( )
				.getResourceFolder( ) );
		options.put( WebViewer.SERVLET_NAME_KEY, WebViewer.VIEWER_DOCUMENT );
		WebViewer.display( model.getFileName( ), options );
	}

}
