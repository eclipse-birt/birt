/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view;

import org.eclipse.birt.chart.examples.view.util.OpenJavaSourceAction;
import org.eclipse.birt.chart.examples.view.util.SaveXMLAction;
import org.eclipse.birt.chart.examples.view.util.Tools;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

/**
 * ChartExamplesView consists of a workbench view including the examples
 * selector and preview canvas.
 *
 * @see ViewPart
 */
public class ChartExamplesView extends ViewPart {

	ChartExamples instance = null;

	static SaveXMLAction sxAction = null;

	static OpenJavaSourceAction opsAction = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.VIEW_CHART_EXAMPLE);

		instance = new ChartExamples(parent);

		final IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbarManager = actionBars.getToolBarManager();
		Tools tools[] = ChartExamples.tools;
		String group = tools[0].group;
		toolbarManager.add(new GroupMarker(group));
		for (int i = 0; i < tools.length; i++) {
			Tools tool = tools[i];
			if (!tool.group.equals(group)) {
				toolbarManager.add(new Separator());
				toolbarManager.add(new GroupMarker(tool.group));
			}
			group = tool.group;
			toolbarManager.appendToGroup(group, initActions(tool, parent));
		}
		actionBars.updateActionBars();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		instance.setFocus();
	}

	/**
	 * Called when the View is to be disposed.
	 */
	@Override
	public void dispose() {
		instance.dispose();
		instance = null;
		super.dispose();
	}

	private Action initActions(Tools tool, Composite cmp) {
		if (tool.name.equals("Save")) //$NON-NLS-1$
		{
			sxAction = new SaveXMLAction(tool, cmp);
			return sxAction;
		} else if (tool.name.equals("Open")) //$NON-NLS-1$
		{
			opsAction = new OpenJavaSourceAction(tool, getViewSite().getWorkbenchWindow());
			return opsAction;
		} else {
			return null;
		}
	}

	public static void setActionsEnabled(boolean bEnabled) {
		sxAction.setEnabled(bEnabled);
		opsAction.setEnabled(bEnabled);
	}
}
