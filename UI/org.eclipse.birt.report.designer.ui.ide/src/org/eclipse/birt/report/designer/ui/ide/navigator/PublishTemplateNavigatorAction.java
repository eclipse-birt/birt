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

package org.eclipse.birt.report.designer.ui.ide.navigator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishTemplateWizard;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * 
 */

public class PublishTemplateNavigatorAction implements IViewActionDelegate
{

	protected IViewPart navigator;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init( IViewPart view )
	{
		navigator = view;
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		IFile file = getSelectedFile( );
		if ( file != null )
		{
			String url = file.getLocation( ).toOSString( );
			try
			{
				ModuleHandle handle = SessionHandleAdapter.getInstance( )
						.getSessionHandle( )
						.openDesign( url );

				if ( handle == null )
				{
					action.setEnabled( false );
					return;
				}

				IEditorPart editor = org.eclipse.birt.report.designer.internal.ui.util.UIUtil.findOpenedEditor( url );

				if ( editor != null && editor.isDirty( ) )
				{
					MessageDialog md = new MessageDialog( UIUtil.getDefaultShell( ),
							Messages.getString( "PublishTemplateAction.SaveBeforeGenerating.dialog.title" ), //$NON-NLS-1$
							null,
							Messages.getFormattedString( "PublishTemplateAction.SaveBeforeGenerating.dialog.message", new Object[]{file.getName( )} ), //$NON-NLS-1$
							MessageDialog.CONFIRM,
							new String[]{
									Messages.getString( "PublishTemplateAction.SaveBeforeGenerating.dialog.button.yes" ), //$NON-NLS-1$
									Messages.getString( "PublishTemplateAction.SaveBeforeGenerating.dialog.button.no" ) //$NON-NLS-1$
							},
							0 );
					switch ( md.open( ) )
					{
						case 0 :
							editor.doSave( null );
							break;
						case 1 :
						default :
					}
				}

				WizardDialog dialog = new BaseWizardDialog( UIUtil.getDefaultShell( ),
						new PublishTemplateWizard( (ReportDesignHandle) handle ) );
				dialog.setPageSize( 500, 250 );
				dialog.open( );

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

	protected IFile getSelectedFile( )
	{
		if ( navigator != null )
		{
			IStructuredSelection selection = (IStructuredSelection) navigator.getViewSite( ).getSelectionProvider( ).getSelection( );
			if ( selection.size( ) == 1
					&& selection.getFirstElement( ) instanceof IFile )
			{
				return (IFile) selection.getFirstElement( );
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{

	}

}
