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
	@Override
	public Object[] getChildren() {
		if (getParent() != null) {
			DebugScriptedDesignVisitor visitor = new DebugScriptedDesignVisitor();
			return visitor.getScriptNodes((ReportDesignHandle) getParent()).toArray();

		}
		return new Object[0];
	}
}
