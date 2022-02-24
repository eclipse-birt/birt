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

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * Text menu manager contains 'undo,redo,cut,copy,paste,select all' menuItem. It
 * displays on textVeiwer.
 * 
 */
class TextMenuManager {

	private Hashtable htActions = new Hashtable();
	private MenuManager manager;

	/**
	 * Constructor to specify the textMenuManager for a text viewer.
	 * 
	 * @param viewer
	 */
	TextMenuManager(TextViewer viewer) {
		manager = new MenuManager();
		Separator separator = new Separator("undo");//$NON-NLS-1$
		manager.add(separator);
		separator = new Separator("copy");//$NON-NLS-1$
		manager.add(separator);
		separator = new Separator("select");//$NON-NLS-1$
		manager.add(separator);
		manager.appendToGroup("undo", getAction("undo", viewer, JdbcPlugin.getResourceString("sqleditor.action.undo"), //$NON-NLS-1$
				ITextOperationTarget.UNDO));
		manager.appendToGroup("undo", getAction("redo", viewer, JdbcPlugin.getResourceString("sqleditor.action.redo"), //$NON-NLS-1$
				ITextOperationTarget.REDO));
		manager.appendToGroup("copy", getAction("cut", viewer, JdbcPlugin.getResourceString("sqleditor.action.cut"), //$NON-NLS-1$
				ITextOperationTarget.CUT));
		manager.appendToGroup("copy", getAction("copy", viewer, JdbcPlugin.getResourceString("sqleditor.action.copy"), //$NON-NLS-1$
				ITextOperationTarget.COPY));
		manager.appendToGroup("copy", getAction("paste", viewer, JdbcPlugin.getResourceString("sqleditor.action.paste"), //$NON-NLS-1$
				ITextOperationTarget.PASTE));
		manager.appendToGroup("select", getAction("selectall", viewer, //$NON-NLS-1$
				JdbcPlugin.getResourceString("sqleditor.action.selectAll"), ITextOperationTarget.SELECT_ALL));

		manager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				Enumeration elements = htActions.elements();
				while (elements.hasMoreElements()) {
					SQLEditorAction action = (SQLEditorAction) elements.nextElement();
					action.update();
				}
			}
		});
	}

	/**
	 * 
	 * @param control
	 * @return
	 */
	public Menu getContextMenu(Control control) {
		return manager.createContextMenu(control);
	}

	/**
	 * 
	 * @param id
	 * @param viewer
	 * @param name
	 * @param operation
	 * @return
	 */
	private final SQLEditorAction getAction(String id, TextViewer viewer, String name, int operation) {
		SQLEditorAction action = (SQLEditorAction) htActions.get(id);
		if (action == null) {
			action = new SQLEditorAction(viewer, name, operation);
			htActions.put(id, action);
		}
		return action;
	}

	/**
	 * SQL editor action set
	 * 
	 */
	static class SQLEditorAction extends Action {

		private int operationCode = -1;
		private TextViewer viewer = null;

		public SQLEditorAction(TextViewer viewer, String text, int operationCode) {
			super(text);
			this.operationCode = operationCode;
			this.viewer = viewer;
		}

		/*
		 * 
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		public void run() {
			viewer.doOperation(operationCode);
		}

		/**
		 * update the operation
		 * 
		 */
		public void update() {
			setEnabled(viewer.canDoOperation(operationCode));
		}

	}

}
