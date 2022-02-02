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

package org.eclipse.birt.report.debug.internal.ui.script.actions;

import java.util.logging.Logger;

import org.eclipse.birt.report.debug.internal.ui.script.outline.ScriptProviderFactory;
import org.eclipse.birt.report.debug.internal.ui.script.outline.node.DebugScriptObjectNode;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Supprt to show the content of the selection.
 */

public class ScriptEditAction extends AbstractViewAction {

	private static final Logger logger = Logger.getLogger(ScriptEditAction.class.getName());

	/**
	 * Constructor
	 * 
	 * @param selectedObject
	 */
	public ScriptEditAction(Object selectedObject) {
		super(selectedObject);
	}

	/**
	 * Constructor
	 * 
	 * @param selectedObject
	 * @param text
	 */
	public ScriptEditAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		if (getSelectionObject() != null && getSelectionObject() instanceof DebugScriptObjectNode) {
			return true;
		}
		return false;
	}

	private Object getSelectionObject() {
		Object obj = super.getSelection();
		if (obj instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) obj;
			if (selection.size() != 1) {// multiple selection
				return null;
			}
			obj = selection.getFirstElement();
		}
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		if (getSelectionObject() == null) {
			return;
		}
		try {
			ScriptProviderFactory.createProvider(getSelectionObject()).performRequest(getSelectionObject(),
					new Request(IRequestConstants.REQUEST_TYPE_EDIT));
		} catch (Exception e) {
			logger.warning(e.getMessage());
		}
	}
}
