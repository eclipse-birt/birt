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
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.ui.actions.Clipboard;

/**
 *
 */

public class CopyHandler extends SelectionHandler {

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

		Object[] selection = getElementHandles().toArray();

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Copy action >> Copy " + Arrays.toString(selection)); //$NON-NLS-1$
		}
		Object cloneElements = DNDUtil.cloneSource(selection);
		if (cloneElements != null) {
			Clipboard.getDefault().setContents(cloneElements);
		}

		return Boolean.TRUE;
	}

}
