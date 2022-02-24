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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupActionFactory;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 */

public class InsertGroupMenuAction extends SelectionAction {

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupMenuAction"; //$NON-NLS-1$

	private MenuManager menu;

	// private Action action = null;

	/**
	 * @param part
	 */
	public InsertGroupMenuAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
	}

	/**
	 * Updates then current menu.
	 * 
	 * @param menu the current menu
	 */
	public void updateMenu(MenuManager menu) {
		this.menu = menu;
		run();
	}

	protected boolean calculateEnabled() {
		return true;

	}

	/**
	 * Runs action.
	 * 
	 */
	public void run() {
		menu.removeAll();
		menu.update(true);
		Action[] actions = InsertGroupActionFactory.getInsertGroupActions(getSelectedObjects());
		for (int i = 0; i < actions.length; i++) {
			menu.add(actions[i]);
		}
		menu.update(true);
		return;
	}

	/**
	 * Gets the first selected object.
	 * 
	 * @return The first selected object
	 */
	protected Object getFirstElement() {
		Object[] array = getElements().toArray();
		if (array.length > 0) {
			return array[0];
		}
		return null;
	}

	/**
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements() {
		return InsertInLayoutUtil.editPart2Model(getSelection()).toList();
	}

}
