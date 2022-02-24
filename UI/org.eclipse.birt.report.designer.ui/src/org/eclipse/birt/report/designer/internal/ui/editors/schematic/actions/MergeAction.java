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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Merges cells action
 */
public class MergeAction extends ContextSelectionAction {

	private static final String ACTION_MSG_MERGE = Messages.getString("MergeAction.actionMsg.merge"); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.Merge"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 * 
	 * @param part current work bench part
	 */
	public MergeAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_MSG_MERGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		if (getRowHandles().isEmpty() && getColumnHandles().isEmpty()) {
			return getTableEditPart() != null && getTableEditPart().canMerge();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
//		if ( Policy.TRACING_ACTIONS )
//		{
//			System.out.println( "Merge action >> Run ..." ); //$NON-NLS-1$
//		}
//		TableEditPart part = getTableEditPart( );
//		if ( part != null && part.canMerge( ) )
//		{
//			part.merge( );
//		}
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.mergeCommand", null); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
