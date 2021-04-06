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
