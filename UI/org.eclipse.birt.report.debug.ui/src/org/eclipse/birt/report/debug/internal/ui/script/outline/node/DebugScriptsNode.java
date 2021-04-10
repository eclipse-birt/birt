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

import org.eclipse.birt.report.designer.core.model.views.outline.ScriptsNode;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Script root node.
 */

public class DebugScriptsNode extends ScriptsNode {

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public DebugScriptsNode(ReportDesignHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.views.outline.ScriptsNode#
	 * getChildren()
	 */
	public Object[] getChildren() {
		if (getParent() != null) {
			DebugScriptedDesignVisitor visitor = new DebugScriptedDesignVisitor();
			return visitor.getScriptNodes((ReportDesignHandle) getParent()).toArray();

		}
		return new Object[0];
	}
}
