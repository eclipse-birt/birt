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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.UseCssInReportDialog;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;


/**
 * 
 */

public class UseCssStyleAction extends AbstractViewAction
{

	public static final String ID = "ImportCSSStyleAction"; //$NON-NLS-1$

	public static final String ACTION_TEXT = Messages.getString("UseCssStyleAction.text"); //$NON-NLS-1$

	public UseCssStyleAction( Object selectedObject )
	{
		this( selectedObject, ACTION_TEXT );
	}

	public UseCssStyleAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		// TODO Auto-generated method stub
		return true;
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		// TODO Auto-generated method stub
		UseCssInReportDialog dialog = new UseCssInReportDialog( );
		String relativeFileName = null;
		dialog.setFileName( relativeFileName );
		if ( dialog.open( ) == Dialog.OK )
		{
			ReportDesignHandle moduleHandle = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			try
			{				
				moduleHandle.addCss( dialog.getFileName( ) );

				// Remove later === begin ===
				PropertyHandle propHandle = (PropertyHandle) moduleHandle.getPropertyHandle( "cssStyleSheets" );
				Iterator iter = propHandle.iterator( );
				while ( iter.hasNext( ) )
				{
					IncludedCssStyleSheetHandle includeCssStyleHandle = (IncludedCssStyleSheetHandle) iter.next( );
					for ( Iterator iter2 = includeCssStyleHandle.iterator( ); iter2.hasNext( ); )
					{
						SharedStyleHandle styleHandle = (SharedStyleHandle) iter2.next( );
					}
				}

				// Remove later === end ===
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}



}
