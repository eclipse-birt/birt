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

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.ui.actions.Clipboard;

/**
 *
 */

public class CopyCellContentsHandler extends SelectionHandler {

	public static final String ID = "org.eclipse.birt.report.designer.ui.command.copyCellContentsCommand"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		Object[] selections = getElementHandles().toArray();

		if (selections.length != 1) {
			return Boolean.FALSE;
		}

		CellHandle cellHandle = (CellHandle) selections[0];

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Copy action >> Copy " + Arrays.toString(selections)); //$NON-NLS-1$
		}
		Object cloneElements = DNDUtil.cloneSource(cellHandle.getContent().getContents().toArray());
		if (cloneElements != null) {
			Clipboard.getDefault().setContents(cloneElements);
		}

		return Boolean.TRUE;
	}

}
