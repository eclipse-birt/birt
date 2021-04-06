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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action include or not the footer of a table.
 */
public class IncludeFooterAction extends InsertRowAction {

	/** action text */
	private static final String ACTION_MSG_INCLUDE_FOOTER = Messages
			.getString("IncludeFooterAction.actionMsg.includeFooter"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeFooterAction"; //$NON-NLS-1$

	/**
	 * Constructs a new action instance.
	 * 
	 * @param part The current work bench part
	 */
	public IncludeFooterAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		// setChecked( true );
		setText(ACTION_MSG_INCLUDE_FOOTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getTableEditPart() != null && !getTableEditPart().isDelete()) {
			TableHandleAdapter adapt = HandleAdapterFactory.getInstance()
					.getTableHandleAdapter(getTableEditPart().getModel());
			return adapt != null && !adapt.hasSlotHandleRow(TableHandleAdapter.FOOTER);
		}
		return false;
	}

	/**
	 * Runs this action.
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Include table footer action >> Run ..."); //$NON-NLS-1$
		}
		getTableEditPart().includeSlotHandle(true, TableHandleAdapter.FOOTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#update()
	 */
	// public void update( )
	// {
	// super.update( );
	// if ( getTableEditPart( ) != null )
	// {
	// TableHandleAdapter adapt = HandleAdapterFactory.getInstance( )
	// .getTableHandleAdapter( getTableEditPart( ).getModel( ) );
	// setChecked( adapt.hasSlotHandleRow( TableHandleAdapter.FOOTER ) );
	// }
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
	 * InsertRowAction#getTableEditPart()
	 */
	protected TableEditPart getTableEditPart() {
		TableEditPart part = super.getTableEditPart();
		if (part instanceof GridEditPart) {
			return null;
		}
		return part;
	}

}