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

package org.eclipse.birt.report.designer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

public class ScriptedElementVisitor {

	public ScriptedElementVisitor() {

	}

	public List getScriptNodes(DesignElementHandle elementHandle) {
		List scriptNodes = new ArrayList();
		List scriptMethods = elementHandle.getMethods();
		if (scriptMethods != null) {
			for (Iterator ite = scriptMethods.iterator(); ite.hasNext();) {
				IElementPropertyDefn elementPropDefn = (IElementPropertyDefn) ite.next();
				String methodName = elementPropDefn.getMethodInfo().getName();
				if (elementHandle.getStringProperty(methodName) != null) {
					ScriptObjectNode scriptElementNode = new ScriptObjectNode(
							elementHandle.getPropertyHandle(methodName));
					scriptNodes.add(scriptElementNode);
				}
			}
		}

		if (elementHandle instanceof ReportItemHandle) {
			ReportItemHandle handle = (ReportItemHandle) elementHandle;
			if (handle.getCurrentView() != null) {
				ScriptedDesignVisitor visitor = new ScriptedDesignVisitor();
				scriptNodes.addAll(visitor.getScriptNodes(handle.getCurrentView()));
			}
		}
		return scriptNodes;
	}
}
