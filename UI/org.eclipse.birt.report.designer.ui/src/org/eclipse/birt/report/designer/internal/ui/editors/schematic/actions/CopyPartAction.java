/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AreaEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MasterPageEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Copy action
 */
public class CopyPartAction extends WrapperSelectionAction
{

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() 
	{
		List objects = this.getSelectedObjects();
		
		for(int i =0;i<objects.size();i++)
		{
			if(objects.get(i) instanceof TableCellEditPart ||
					objects.get(i) instanceof ReportDesignEditPart ||
					objects.get(i) instanceof MasterPageEditPart |
					objects.get(i) instanceof AreaEditPart ||
					objects.get(i) instanceof ListBandEditPart)
			{
				return false;
			}
		}
		return super.calculateEnabled();
	}
	/**
	 * Create a new copy action with given selection and text
	 * 
	 * @param part
	 *            the selected object,which cannot be null
	 */
	public CopyPartAction( IWorkbenchPart part )
	{
		super( part );
		ISharedImages shareImages = PlatformUI.getWorkbench( )
				.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_COPY ) );
		setDisabledImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_COPY_DISABLED ) );
		setAccelerator( SWT.CTRL | 'C' );//$NON-NLS-1$
	}

	public String getId( )
	{
		return ActionFactory.COPY.getId( );
	}

	/**
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.WrapperSelectionAction#createActionHandler(org.eclipse.jface.viewers.ISelection)
	 */
	protected IAction createActionHandler( ISelection model )
	{
		return new CopyAction( model );
	}
}