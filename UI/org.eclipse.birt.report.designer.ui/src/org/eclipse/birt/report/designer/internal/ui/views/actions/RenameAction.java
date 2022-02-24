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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.RenameInputDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ContentElementHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This class represents the rename action
 */

public class RenameAction extends AbstractViewerAction {

	/**
	 * the default text
	 */
	public static final String TEXT = Messages.getString("RenameAction.text"); //$NON-NLS-1$

	private TreeItem selectedItem;

	private String originalName;

	private static final String ERROR_TITLE = Messages.getString("RenameInlineTool.DialogTitle.RenameFailed"); //$NON-NLS-1$

	private static final String TRANS_LABEL = Messages.getString("RenameInlineTool.TransLabel.Rename"); //$NON-NLS-1$

	/**
	 * Create a new rename action under the specific viewer
	 * 
	 * @param sourceViewer the source viewer
	 * 
	 */
	public RenameAction(TreeViewer sourceViewer) {
		this(sourceViewer, TEXT);
	}

	/**
	 * Create a new rename action under the specific viewer with the given text
	 * 
	 * @param sourceViewer the source viewer
	 * @param text         the text of the action
	 */
	public RenameAction(TreeViewer sourceViewer, String text) {
		super(sourceViewer, text);
		setAccelerator(SWT.F2);
		if (isEnabled()) {
			selectedItem = getSelectedItems()[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled() {
		if (getSelectedObjects().size() != 1) {// multiple selection or no selection
			return false;
		}
		Object obj = super.getSelectedObjects().getFirstElement();

		if (obj instanceof EmbeddedImageHandle) {
			return true;
		}
		if (obj instanceof SharedStyleHandle
				&& ((SharedStyleHandle) obj).getContainer() instanceof ReportItemThemeHandle) {
			return false;
		}
		if (obj instanceof ReportElementHandle) {
			if (obj instanceof GroupHandle) {
				return !((GroupHandle) obj).getPropertyHandle(IGroupElementModel.GROUP_NAME_PROP).isReadOnly();
			}
			return ((ReportElementHandle) obj).getDefn().getNameOption() != MetaDataConstants.NO_NAME
					&& ((ReportElementHandle) obj).canEdit();
		}
		if (obj instanceof ContentElementHandle) {
			return ((ContentElementHandle) obj).getDefn().getNameOption() != MetaDataConstants.NO_NAME
					&& ((ContentElementHandle) obj).canEdit();
		}
		// No report element selected
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println(
					"Rename action >> Runs with " + DEUtil.getDisplayLabel(getSelectedObjects().getFirstElement())); //$NON-NLS-1$
		}
		doRename();
	}

	private void doRename() {
		if (selectedItem.getData() instanceof DesignElementHandle
				|| selectedItem.getData() instanceof EmbeddedImageHandle) {
			initOriginalName();

			TreeItem[] items = selectedItem.getParentItem().getItems();
			String[] existedNames = new String[items.length];
			for (int i = 0; i < items.length; i++) {
				if (items[i].getData() instanceof DesignElementHandle) {
					existedNames[i] = ((DesignElementHandle) items[i].getData()).getName();
				} else if (items[i].getData() instanceof EmbeddedImageHandle) {
					existedNames[i] = ((EmbeddedImageHandle) items[i].getData()).getName();
				}
			}

			RenameInputDialog inputDialog = new RenameInputDialog(selectedItem.getParent().getShell(),
					Messages.getString("RenameInputDialog.DialogTitle"), //$NON-NLS-1$
					Messages.getString("RenameInputDialog.DialogMessage"), //$NON-NLS-1$
					originalName, existedNames, IHelpContextIds.RENAME_INPUT_DIALOG_ID);
			inputDialog.create();
			if (inputDialog.open() == Window.OK) {
				saveChanges(inputDialog.getResult().toString().trim());
			}
		}
	}

	private void initOriginalName() {
		if (selectedItem.getData() instanceof DesignElementHandle) {
			originalName = ((DesignElementHandle) selectedItem.getData()).getName();
		}
		if (selectedItem.getData() instanceof EmbeddedImageHandle) {
			originalName = ((EmbeddedImageHandle) selectedItem.getData()).getName();
		}

		if (originalName == null) {
			originalName = ""; //$NON-NLS-1$
		}
	}

	private void saveChanges(String newName) {
		if (!newName.equals(originalName)) {
			if (!rename(selectedItem.getData(), newName)) {
				// failed to rename, do again
				doRename();
				return;
			}
		}
	}

	/**
	 * Perform renaming
	 * 
	 * @param handle  the handle of the element to rename
	 * @param newName the newName to set
	 * @return Returns true if perform successfully,or false if failed
	 */
	private boolean rename(Object handle, String newName) {
		if (newName.length() == 0) {
			newName = null;
		}
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
			return false;
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(TRANS_LABEL + " " + DEUtil.getDisplayLabel(handle)); //$NON-NLS-1$
		try {
			if (handle instanceof DesignElementHandle) {
				((DesignElementHandle) handle).setName(newName);
			}

			if (handle instanceof EmbeddedImageHandle) {
				((EmbeddedImageHandle) handle).setName(newName);
			}
			stack.commit();
		} catch (NameException e) {
			ExceptionHandler.handle(e, ERROR_TITLE, e.getLocalizedMessage());
			stack.rollback();
			return false;
		} catch (SemanticException e) {
			ExceptionHandler.handle(e, ERROR_TITLE, e.getLocalizedMessage());
			stack.rollback();
			// If set EmbeddedImage name error, then use former name;
			return true;
		}
		return true;
	}
}
