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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.DataBindingDialog;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.PropertyProcessor;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Edit biding action
 */
public class EditBindingAction extends InsertRowAction
{

	public static final String ID = "org.eclipse.birt.report.designer.action.editBinding";

	/**
	 * @param part
	 */
	public EditBindingAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( Messages.getString( "DesignerActionBarContributor.menu.element.editDataBinding" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		//Get the first item in the list and pass the model object to the
		// dialog
		TableEditPart editPart = getTableEditPart( );
		if ( editPart != null )
		{
			CommandStack stack = SessionHandleAdapter.getInstance( )
					.getReportDesign( )
					.getActivityStack( );
			stack.startTrans( Messages.getString( "DesignerActionBarContributor.menu.element.editDataBinding" ) ); //$NON-NLS-1$
			PropertyProcessor processor = new PropertyProcessor( ReportDesignConstants.TABLE_ITEM,
					ReportItem.DATA_SET_PROP );
			ArrayList items = new ArrayList( );
			items.add( editPart.getModel( ) );
			String currentDataSetName = processor.getStringValue( items );
			DataBindingDialog dialog = new DataBindingDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					(DesignElementHandle) editPart.getModel( ) );

			if ( dialog.open( ) == Dialog.OK )
			{
				String newDataSetName = processor.getStringValue( items );
				if ( !currentDataSetName.equals( newDataSetName ) )
				{
					if ( StringUtil.isBlank( currentDataSetName )
							|| MessageDialog.openQuestion( UIUtil.getDefaultShell( ),
									Messages.getString( "dataBinding.title.changeDataSet" ), Messages.getString( "dataBinding.message.changeDataSet" ) ) ) //$NON-NLS-1$
					{
						stack.commit( );
						return;
					}
				}
			}
			stack.rollback( );
		}
	}
}