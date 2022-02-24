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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 */

public class SplitCellHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Split action >> Run ..."); //$NON-NLS-1$
		}
		if (getTableEditPart() != null) {
			getTableEditPart().splitCell(getTableCellEditPart());
		}

		return Boolean.TRUE;
	}

	/**
	 * Gets cell edit part.
	 * 
	 * @return current table cell edit part
	 */
	private TableCellEditPart getTableCellEditPart() {
		return (TableCellEditPart) getSelectedObjects().get(0);
	}
}
