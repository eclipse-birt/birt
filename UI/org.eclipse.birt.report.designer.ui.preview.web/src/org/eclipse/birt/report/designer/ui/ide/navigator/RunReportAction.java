/* Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.ide.navigator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;

/**
 * The action to run a report in navigator view
 */
public class RunReportAction extends AbstractViewAction
{

	protected boolean prePreview( )
	{
		PreviewUtil.clearSystemProperties();
		return true;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		if ( !prePreview( ) )
		{
			return;
		}

		IFile file = getSelectedFile( );
		if ( file != null )
		{
			// String url = MessageFormat.format( PATTERN, new Object[]{
			// file.getLocation( ).toString( )
			// } );
			String url = file.getLocation( ).toOSString( );
			try
			{
				ModuleHandle handle = SessionHandleAdapter.getInstance( )
						.getSessionHandle( )
						.openDesign( url );

				if ( !UIUtil.canPreviewWithErrors( handle ) )
					return;

				Map options = new HashMap( );
				options.put( WebViewer.FORMAT_KEY, WebViewer.HTML );
				options.put( WebViewer.RESOURCE_FOLDER_KEY,
						ReportPlugin.getDefault( )
								.getResourceFolder( file.getProject( ) ) );

				WebViewer.display( url, options );
				handle.close( );
			}
			catch ( Exception e )
			{
				ExceptionUtil.handle( e );
				return;
			}
		}
		else
		{
			action.setEnabled( false );
		}
	}
}
