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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowBelowAction;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.actions.ActionFactory;

/**
 * The factory to create all global actions
 */

public class GlobalActionFactory {

	public final static String COPY = ActionFactory.COPY.getId();
	public final static String CUT = ActionFactory.CUT.getId();
	public final static String PASTE = ActionFactory.PASTE.getId();
	public final static String DELETE = ActionFactory.DELETE.getId();
	public final static String UNDO = ActionFactory.UNDO.getId();
	public final static String REDO = ActionFactory.REDO.getId();

	public final static String[] GLOBAL_SELECTION_ACTIONS = { COPY, CUT, PASTE, DELETE };

	public final static String[] GLOBAL_STACK_ACTIONS = { UNDO, REDO };

	public final static String[] GLOBAL_INSERT_ACTIONS = { GeneralInsertMenuAction.INSERT_TEXT_ID,
			GeneralInsertMenuAction.INSERT_LABEL_ID, GeneralInsertMenuAction.INSERT_DATA_ID,
			GeneralInsertMenuAction.INSERT_IMAGE_ID, GeneralInsertMenuAction.INSERT_GRID_ID,
			GeneralInsertMenuAction.INSERT_LIST_ID, GeneralInsertMenuAction.INSERT_TABLE_ID,
			GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_ID, };

	public final static String[] GLOBAL_ELEMENT_ACTIONS = { InsertRowAboveAction.ID, InsertRowBelowAction.ID, };

	public static IAction createSelectionAction(String id, ISelectionProvider provider) {
		assert id != null;
		assert provider != null;
		if (COPY.equals(id)) {
			return new GlobalCopyAction(provider);
		} else if (CUT.equals(id)) {
			return new GlobalCutAction(provider);
		} else if (PASTE.equals(id)) {
			return new GlobalPasteAction(provider);
		} else if (DELETE.equals(id)) {
			return new GlobalDeleteAction(provider);
		}
		String elementType = null;
		if (GeneralInsertMenuAction.INSERT_TEXT_ID.equals(id)) {
			elementType = ReportDesignConstants.TEXT_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_LABEL_ID.equals(id)) {
			elementType = ReportDesignConstants.LABEL_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_DATA_ID.equals(id)) {
			elementType = ReportDesignConstants.DATA_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_IMAGE_ID.equals(id)) {
			elementType = ReportDesignConstants.IMAGE_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_GRID_ID.equals(id)) {
			elementType = ReportDesignConstants.GRID_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_LIST_ID.equals(id)) {
			elementType = ReportDesignConstants.LIST_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_TABLE_ID.equals(id)) {
			elementType = ReportDesignConstants.TABLE_ITEM;
		} else if (GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_ID.equals(id)) {
			elementType = ReportDesignConstants.TEXT_DATA_ITEM;
		} else if (InsertRowAboveAction.ID.equals(id)) {
			return new GlobalInsertRowAction(provider, id, InsertAction.ABOVE);
		} else if (InsertRowBelowAction.ID.equals(id)) {
			return new GlobalInsertRowAction(provider, id, InsertAction.BELOW);
		} else if (DEUtil.getMetaDataDictionary().getExtension(id) != null) {
			elementType = id;
		}
		if (elementType != null) {
			GlobalInsertAction action = new GlobalInsertAction(provider, id, elementType);
			action.setText(DEUtil.getElementDefn(elementType).getDisplayName());
			return action;
		}
		return null;
	}

	public static IAction createStackAction(String id, CommandStack stack) {
		assert id != null;
		assert stack != null;
		GlobalStackActionEntry entry = (GlobalStackActionEntry) stackActionEntrys.get(stack);
		if (entry == null) {
			entry = new GlobalStackActionEntry(stack);
			stackActionEntrys.put(stack, entry);
		}
		return entry.getAction(id);
	}

	public static void removeStackActions(CommandStack stack) {
		stackActionEntrys.remove(stack);
	}

	private static Map stackActionEntrys = new HashMap();

	private static class GlobalStackActionEntry {

		private GlobalUndoAction undoAction = null;
		private GlobalRedoAction redoAction = null;
		private CommandStack stack;

		public GlobalStackActionEntry(CommandStack stack) {
			this.stack = stack;
		}

		public GlobalStackAction getAction(String id) {
			GlobalStackAction action = null;
			if (UNDO.equals(id)) {
				if (undoAction == null) {
					undoAction = new GlobalUndoAction(stack);
				}
				action = undoAction;
			} else if (REDO.equals(id)) {
				if (redoAction == null) {
					redoAction = new GlobalRedoAction(stack);
				}
				action = redoAction;
			}
			if (action != null) {
				action.update();
			}
			return action;
		}
	}

}
