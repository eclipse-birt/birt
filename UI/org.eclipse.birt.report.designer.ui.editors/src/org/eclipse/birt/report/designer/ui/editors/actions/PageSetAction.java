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

package org.eclipse.birt.report.designer.ui.editors.actions;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Default implement of the action to change page
 */

public class PageSetAction extends Action implements UpdateAction {

	private String pageId;

	public PageSetAction(String text, String id) {
		super(text, IAction.AS_RADIO_BUTTON);
		pageId = id;
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.UpdateAction#update()
	 */
	public void update() {
		FormEditor editor = UIUtil.getActiveReportEditor();
		setEnabled(editor != null);
		// Add judge the id is null,see
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=305851
		// It is a strange because the BIRD editor is not null.Maybe conflict with
		// others product?Need deeply research.
		if (editor != null && editor.getActivePageInstance() != null
				&& editor.getActivePageInstance().getId() != null) {
			setChecked(editor.getActivePageInstance().getId().equals(pageId));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		UIUtil.getActiveReportEditor().setActivePage(pageId);
	}
}
