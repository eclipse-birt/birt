/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.model.views.outline;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

/**
 * Represents the script method node of a report element
 */
public class ScriptObjectNode implements IScriptTreeNode, IMenuListener {
	private static final String ACTION_TEXT = Messages.getString("ScriptObjectNode_0"); //$NON-NLS-1$
	private PropertyHandle parent;

	public ScriptObjectNode(PropertyHandle parent) {
		this.parent = parent;
	}

	@Override
	public Object[] getChildren() {
		// TODO Auto-generated method stub
		return new Object[0];
	}

	@Override
	public Object getParent() {
		return this.parent;
	}

	public String getText() {
		return parent.getPropertyDefn().getName();
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		manager.add(new ResetScriptAction());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == this) {
			return true;
		}
		if (arg0 instanceof ScriptObjectNode) {
			return parent == null ? (((ScriptObjectNode) arg0).parent == null)
					: parent.equals(((ScriptObjectNode) arg0).parent);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 13;
		if (parent != null) {
			hashCode += parent.hashCode() * 7;
		}
		return hashCode;
	}

	class ResetScriptAction extends Action {
		ResetScriptAction() {
			super(ACTION_TEXT);
		}

		@Override
		public void run() {
			CommandStack commandStack = parent.getElementHandle().getModuleHandle().getCommandStack();
			commandStack.startPersistentTrans(ACTION_TEXT);
			try {
				reset();
			} catch (SemanticException e) {
				commandStack.rollbackAll();
				return;
			}
			commandStack.commit();
		}
	}

	public void reset() throws SemanticException {
		parent.setValue(null);
	}
}
