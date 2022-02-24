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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.core.model.IMixedHandle;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements the delete action in the outline view
 * 
 * 
 */
public class DeleteAction extends AbstractElementAction {

	private static final String DEFAULT_TEXT = Messages.getString("DeleteAction.text"); //$NON-NLS-1$

	private boolean hasExecuted = false;

	/**
	 * Create a new delete action with given selection and default text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * 
	 */
	public DeleteAction(Object selectedObject) {
		this(selectedObject, DEFAULT_TEXT);
	}

	/**
	 * Create a new delete action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public DeleteAction(Object selectedObject, String text) {
		super(selectedObject, text);
		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		setAccelerator(SWT.DEL);
	}

	protected boolean doAction() throws Exception {

		Object selection = getSelection();
		if (selection instanceof IMixedHandle) {
			selection = ((IMixedHandle) selection).getChildren().toArray();
		}
		if (selection != null && selection instanceof StructuredSelection) {
			Object element = ((StructuredSelection) selection).getFirstElement();
			if (element != null && element instanceof LibraryHandle) {
				if (((LibraryHandle) element).getHostHandle() != null) {
					return new RemoveLibraryAction(element).doAction();
				}
			}
		}
		if (selection != null) {
			CommandUtils.setVariable(ICommandParameterNameContants.SELECTION, selection);
		}

		Object exeResult = null;
		exeResult = CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.deleteCommand", //$NON-NLS-1$
				null);

		hasExecuted = ((Boolean) exeResult).booleanValue();
		CommandUtils.removeVariable(ICommandParameterNameContants.SELECTION);

		return Boolean.TRUE.equals(exeResult);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled() {
		Object selection = getSelection();
		if (selection instanceof IMixedHandle) {
			selection = ((IMixedHandle) selection).getChildren().toArray();
		} else if (selection != null && selection instanceof StructuredSelection) {
			Object element = ((StructuredSelection) selection).getFirstElement();
			if (element != null && element instanceof LibraryHandle) {
				if (((LibraryHandle) element).getHostHandle() != null) {
					return true;
				}
			}
		}
		Command cmd = createDeleteCommand(selection);
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#getTransactionLabel()
	 */

	protected String getTransactionLabel() {
		if (getSelection() instanceof IStructuredSelection) {
			return Messages.getString("DeleteAction.trans"); //$NON-NLS-1$
		}
		return DEFAULT_TEXT + " " + DEUtil.getDisplayLabel(getSelection()); //$NON-NLS-1$
	}

	protected Command createDeleteCommand(Object objects) {
		return new DeleteCommand(objects);
	}

	/**
	 * Returns if user press OK to run the action.
	 */
	public boolean hasExecuted() {
		return hasExecuted;
	}

}
