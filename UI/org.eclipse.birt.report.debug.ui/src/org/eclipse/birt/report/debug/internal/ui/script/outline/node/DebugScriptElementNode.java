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

package org.eclipse.birt.report.debug.internal.ui.script.outline.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.jface.action.IMenuManager;

/**
 * Scipt element node(DebugScriptsNode children).
 */

public class DebugScriptElementNode extends ScriptElementNode {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode#
	 * menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		// donothing now
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public DebugScriptElementNode(DesignElementHandle parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode#
	 * getChildren()
	 */
	public Object[] getChildren() {
		return getScriptNodes((DesignElementHandle) getParent()).toArray();
	}

	/**
	 * Gets the children.
	 * 
	 * @param elementHandle
	 * @return
	 */
	public List getScriptNodes(DesignElementHandle elementHandle) {
		List scriptNodes = new ArrayList();
		List scriptMethods = elementHandle.getMethods();
		if (scriptMethods != null) {
			for (Iterator ite = scriptMethods.iterator(); ite.hasNext();) {
				IElementPropertyDefn elementPropDefn = (IElementPropertyDefn) ite.next();
				PropertyHandle handle = elementHandle.getPropertyHandle(elementPropDefn.getName());
				String methodName = elementPropDefn.getMethodInfo().getName();
				if (elementHandle.getStringProperty(methodName) != null) {
					DebugScriptObjectNode scriptElementNode = new DebugScriptObjectNode(handle);
					scriptElementNode.setNodeParent(this);
					scriptNodes.add(scriptElementNode);
				}
			}
		}
		return scriptNodes;
	}
}
