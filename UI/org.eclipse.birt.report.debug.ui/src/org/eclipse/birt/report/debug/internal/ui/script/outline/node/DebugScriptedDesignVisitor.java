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
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Vistor to get the script children.
 */
public class DebugScriptedDesignVisitor extends DesignVisitor {
	private List scriptNodes = new ArrayList();

	/**
	 * Constructor
	 */
	public DebugScriptedDesignVisitor() {
		super();
	}

	/**
	 * @param handle
	 * @return
	 */
	public List getScriptNodes(ReportDesignHandle handle) {
		scriptNodes.clear();
		apply(handle);
		return scriptNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignVisitor#visitDesignElement(org.
	 * eclipse.birt.report.model.api.DesignElementHandle)
	 */
	public void visitDesignElement(DesignElementHandle elementHandle) {
		List scriptMethods = elementHandle.getMethods();
		if (scriptMethods != null) {
			for (Iterator ite = scriptMethods.iterator(); ite.hasNext();) {
				IElementPropertyDefn elementPropDefn = (IElementPropertyDefn) ite.next();
				String methodName = elementPropDefn.getMethodInfo().getName();

				if (elementHandle.getStringProperty(methodName) != null) {
					ScriptElementNode scriptElementNode = new DebugScriptElementNode(elementHandle);
					scriptNodes.add(scriptElementNode);
					break;
				}
			}
		}

		for (int i = 0; i < elementHandle.getDefn().getSlotCount(); i++) {
			visitContents(elementHandle.getSlot(i));
		}
		for (int i = 0; i < elementHandle.getDefn().getContents().size(); i++) {
			visitContents(elementHandle, ((PropertyDefn) elementHandle.getDefn().getContents().get(i)).getName());
		}
	}
}
