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

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.ui.actions.ChangeRulerUnitAction;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 *
 */

public class EditorRulerContextMenuProvider extends ContextMenuProvider {

	/**
	 * @param viewer
	 */
	public EditorRulerContextMenuProvider(EditPartViewer viewer) {
		super(viewer);
	}

	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		IChoiceSet choiceSet;

		choiceSet = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.REPORT_DESIGN_ELEMENT,
				ReportDesignHandle.UNITS_PROP);

		if (choiceSet == null) {
			return;
		}

		int len = choiceSet.getChoices().length;
		for (int i = 0; i < len; i++) {
			IChoice ch = choiceSet.getChoices()[i];
			ChangeRulerUnitAction action = new ChangeRulerUnitAction(ch.getName(), ch.getDisplayName());
			menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
		}
	}

	/**
	 * Gets the current selection.
	 *
	 * @return The current selection
	 */
	protected ISelection getSelection() {
		return getViewer().getSelection();
	}

	/**
	 * Returns a <code>List</code> containing the currently selected objects.
	 *
	 * @return A List containing the currently selected objects
	 */
	protected List getSelectedObjects() {
		if (!(getSelection() instanceof IStructuredSelection)) {
			return Collections.EMPTY_LIST;
		}
		return ((IStructuredSelection) getSelection()).toList();
	}
}
