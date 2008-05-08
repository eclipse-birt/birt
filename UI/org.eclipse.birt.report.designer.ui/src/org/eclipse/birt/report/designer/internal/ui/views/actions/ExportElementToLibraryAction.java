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

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ExportElementDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class ExportElementToLibraryAction extends AbstractViewAction
{

	private static final String DISPLAY_TEXT = Messages.getString( "ExportToLibraryAction.action.text" ); //$NON-NLS-1$

	public ExportElementToLibraryAction( Object selectedObject )
	{
		super( selectedObject, DISPLAY_TEXT );
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled( )
	{
		// will implement it later.
		Object selection = getSelection();
		if(selection instanceof StructuredSelection)
		{
			selection = ((StructuredSelection)selection).getFirstElement();
		}
		if(selection instanceof ModuleHandle)
		{
			return false;
		}
		if(selection instanceof DesignElementHandle || selection instanceof StructureHandle)
		{
			return true;
		}
		return false;
				
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{

//		ExportReportWizard exportReportWizard = new ExportReportWizard( );
//		WizardDialog wDialog = new WizardDialog( UIUtil.getDefaultShell( ),
//				exportReportWizard );
//		wDialog.setPageSize( 500, 250 );
//		wDialog.open( );
		
		ExportElementDialog dialog = new ExportElementDialog(getSelection());
		dialog.open();
	}
	
}
