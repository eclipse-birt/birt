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

package org.eclipse.birt.report.debug.internal.ui.script.outline.node;

import org.eclipse.birt.report.debug.internal.ui.script.actions.ScriptEditAction;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

/**
 * Script opject(DebugScriptElementNode children).
 */

public class DebugScriptObjectNode extends ScriptObjectNode implements IMenuListener {

	private Object nodeParent;
	private PropertyHandle propertyHandle;
	private static final String TEXT = "Show Source"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param parent
	 */
	public DebugScriptObjectNode(PropertyHandle handle) {
		super(handle);
		this.propertyHandle = handle;
	}

	/**
	 * Gets the property handle
	 * 
	 * @return
	 */
	public PropertyHandle getPropertyHandle() {
		return propertyHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.
	 * action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		manager.add(new ScriptEditAction(this, TEXT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode#
	 * equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		boolean bool = super.equals(obj);
		if (obj instanceof DebugScriptObjectNode) {
			bool = bool || ((DebugScriptObjectNode) obj).propertyHandle.equals(propertyHandle);
		}
		return bool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode#
	 * getParent()
	 */
	public Object getParent() {
		if (nodeParent != null) {
			return nodeParent;
		}
		return super.getParent();
	}

	/**
	 * Sets the node parent
	 * 
	 * @param nodeParent
	 */
	public void setNodeParent(Object nodeParent) {
		this.nodeParent = nodeParent;
	}
}
