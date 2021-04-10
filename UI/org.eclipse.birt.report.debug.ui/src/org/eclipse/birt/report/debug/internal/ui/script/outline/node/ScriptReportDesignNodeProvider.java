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

import org.eclipse.birt.report.debug.internal.ui.script.actions.RefreshAction;
import org.eclipse.birt.report.designer.internal.ui.views.outline.providers.ReportDesignNodeProvider;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Script debugger outline root element provider.
 */

public class ScriptReportDesignNodeProvider extends ReportDesignNodeProvider {
	private static final String TEXT = "Refresh"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * ReportDesignNodeProvider#createContextMenu(org.eclipse.jface.viewers.
	 * TreeViewer, java.lang.Object, org.eclipse.jface.action.IMenuManager)
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		// now only add the refresh action
		menu.add(new RefreshAction(sourceViewer, object, TEXT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * ReportDesignNodeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object model) {
		ReportDesignHandle handle = ((ReportDesignHandle) model);
		ArrayList list = new ArrayList();
		list.add(new DebugScriptsNode(handle));

		return list.toArray();
	}
}
