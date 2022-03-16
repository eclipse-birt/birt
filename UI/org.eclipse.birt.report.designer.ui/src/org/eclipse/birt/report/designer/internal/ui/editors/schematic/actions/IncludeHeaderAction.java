/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
public class IncludeHeaderAction extends InsertRowAction {

	/** action text */
	private static final String ACTION_MSG_INCLUDE_HEADER = Messages
			.getString("IncludeHeaderAction.actionMsg.includeHeader"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeHeaderAction"; //$NON-NLS-1$

	/**
	 * Constructs a new action instance.
	 *
	 * @param part The current work bench part
	 */
	public IncludeHeaderAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		// setChecked( true );
		setText(ACTION_MSG_INCLUDE_HEADER);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		if (getTableEditPart() != null && !getTableEditPart().isDelete()) {
			TableHandleAdapter adapt = HandleAdapterFactory.getInstance()
					.getTableHandleAdapter(getTableEditPart().getModel());
			return adapt != null && !adapt.hasSlotHandleRow(TableHandleAdapter.HEADER);
		}
		return false;
	}

	/**
	 * Runs this action.
	 */
	@Override
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Include table header action >> Run ..."); //$NON-NLS-1$
		}
		getTableEditPart().includeSlotHandle(true, TableHandleAdapter.HEADER);
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
	// setChecked( adapt.hasSlotHandleRow( TableHandleAdapter.HEADER ) );
	// }
	// }
	//
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
	 * InsertRowAction#getTableEditPart()
	 */
	@Override
	protected TableEditPart getTableEditPart() {
		TableEditPart part = super.getTableEditPart();
		if (part instanceof GridEditPart) {
			return null;
		}
		return part;
	}

}
