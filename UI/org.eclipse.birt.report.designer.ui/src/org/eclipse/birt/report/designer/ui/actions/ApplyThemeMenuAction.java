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
package org.eclipse.birt.report.designer.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

public class ApplyThemeMenuAction extends MenuUpdateAction {

	public static final String ID = "apply theme menu"; //$NON-NLS-1$

	public ApplyThemeMenuAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
	}

	protected List getItems() {
		ArrayList actionList = new ArrayList();

		ThemeHandle currentTheme = getThemeHandle();
		ApplyThemeAction reset = new ApplyThemeAction(null);
		reset.setSelection(getSelection());
		reset.setChecked(currentTheme == null);
		actionList.add(reset);
		actionList.add(null);// Adds separator

		Iterator iterator = DEUtil.getThemes();
		if (iterator != null) {
			while (iterator.hasNext()) {
				ThemeHandle handle = (ThemeHandle) iterator.next();
				ApplyThemeAction action = new ApplyThemeAction(handle);
				action.setSelection(getSelection());
				action.setChecked(currentTheme == handle);
				actionList.add(action);
			}
		}
		return actionList;
	}

	private ThemeHandle getThemeHandle() {
		IStructuredSelection selection = null;
		if (getSelection() instanceof IStructuredSelection) {
			selection = (IStructuredSelection) getSelection();
		}

		if (selection != null && !selection.isEmpty() && selection.getFirstElement() instanceof ReportDesignEditPart) {
			return ((ModuleHandle) ((ReportDesignEditPart) selection.getFirstElement()).getModel()).getTheme();
		}
		return null;
	}
}
