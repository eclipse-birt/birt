/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	@Override
	public void buildContextMenu(IMenuManager menu) {
		menu.removeAll();
		for (int i = 0; i < actionList.size(); i++) {
			menu.add(actionList.get(i));
		}
	}

	private List<IAction> actionList = new ArrayList<>();

	public void addAction(IAction action) {
		actionList.add(action);
	}

}
