/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.ContextMenuProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;

public class SampleContextMenuProvider extends ContextMenuProvider {

	public SampleContextMenuProvider(ReportExamplesView view) {
		super(view.instance.getTreeViewer());
	}

	public void buildContextMenu(IMenuManager menu) {
		menu.removeAll();
		for (int i = 0; i < actionList.size(); i++) {
			menu.add(actionList.get(i));
		}
	}

	private List<IAction> actionList = new ArrayList<IAction>();

	public void addAction(IAction action) {
		actionList.add(action);
	}

}
