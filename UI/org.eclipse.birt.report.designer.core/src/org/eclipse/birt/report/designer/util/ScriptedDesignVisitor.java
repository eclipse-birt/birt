/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * 
 * Visit the Design Element Handle has scripts
 */
public class ScriptedDesignVisitor extends DesignVisitor {

	private List scriptNodes = new ArrayList();

	public ScriptedDesignVisitor() {
		super();
	}

	public List getScriptNodes(ModuleHandle handle) {
		return getScriptNodes((DesignElementHandle) handle);
	}

	public List getScriptNodes(DesignElementHandle handle) {
		scriptNodes.clear();
		apply(handle);
		return scriptNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.model.api.DesignVisitor#visitDesignElement(org.eclipse.birt.
	 * model.api.DesignElementHandle)
	 */
	public void visitDesignElement(DesignElementHandle elementHandle) {
		List scriptMethods = elementHandle.getMethods();
		boolean hasCurrentView = false;
		if (elementHandle instanceof ReportItemHandle) {
			ReportItemHandle handle = (ReportItemHandle) elementHandle;

			if (handle.getCurrentView() != null) {
				List currentScriptMethods = handle.getCurrentView().getMethods();
				for (Iterator ite = currentScriptMethods.iterator(); ite.hasNext();) {
					IElementPropertyDefn elementPropDefn = (IElementPropertyDefn) ite.next();
					String methodName = elementPropDefn.getMethodInfo().getName();
					if (handle.getCurrentView().getStringProperty(methodName) != null) {
						hasCurrentView = true;
						break;
					}
				}
			}
		}
		if (scriptMethods != null) {
			for (Iterator ite = scriptMethods.iterator(); ite.hasNext();) {
				IElementPropertyDefn elementPropDefn = (IElementPropertyDefn) ite.next();
				String methodName = elementPropDefn.getMethodInfo().getName();
				if (elementHandle.getStringProperty(methodName) != null || hasCurrentView) {
					ScriptElementNode scriptElementNode = new ScriptElementNode(elementHandle);
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
