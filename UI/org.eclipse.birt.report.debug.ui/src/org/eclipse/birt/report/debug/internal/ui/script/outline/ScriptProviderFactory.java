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

package org.eclipse.birt.report.debug.internal.ui.script.outline;

import org.eclipse.birt.report.debug.internal.ui.script.outline.node.ScriptDebugTreeNodeProvider;
import org.eclipse.birt.report.debug.internal.ui.script.outline.node.ScriptReportDesignNodeProvider;
import org.eclipse.birt.report.designer.core.model.views.outline.IScriptTreeNode;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * Factory to create node provider
 */

public class ScriptProviderFactory {
	static private DefaultNodeProvider defaultProvider = new DefaultNodeProvider();

	/**
	 * Gets the default provider
	 * 
	 * @return Returns the default provider
	 */
	public static DefaultNodeProvider getDefaultProvider() {
		return defaultProvider;
	}

	/**
	 * Create the povider
	 * 
	 * @param object
	 * @return
	 */
	public static INodeProvider createProvider(Object object) {
		if (object instanceof ReportDesignHandle) {
			return new ScriptReportDesignNodeProvider();
		}
		if (object instanceof IScriptTreeNode) {
			return new ScriptDebugTreeNodeProvider();
		}

		return getDefaultProvider();
	}
}
