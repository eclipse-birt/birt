/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Paste action
 */
public class PasteAction extends AbstractViewAction {

	private static final String DEFAULT_TEXT = Messages.getString("PasteAction.text"); //$NON-NLS-1$

	/**
	 * Create a new paste action with given selection and default text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * 
	 */
	public PasteAction(Object selectedObject) {
		this(selectedObject, DEFAULT_TEXT);
	}

	/**
	 * Create a new paste action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public PasteAction(Object selectedObject, String text) {
		super(selectedObject, text);
		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setAccelerator(SWT.CTRL | 'V');
	}

	/**
	 * Runs this action. Copies the content. Each action implementation must define
	 * the steps needed to carry out this action. The default implementation of this
	 * method in <code>Action</code> does nothing.
	 */
	public void run() {
		List infoList = new ArrayList();
		if (!canPaste(infoList)) {
			String message = null;
			if (infoList.size() != 0) {
				message = ((SemanticException) infoList.get(0)).getLocalizedMessage();
				if (message != null) {
					MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
							Messages.getString("PasteAction.dlg.title"), //$NON-NLS-1$
							null, message, MessageDialog.INFORMATION,
							new String[] { Messages.getString("PasteAction.ok") //$NON-NLS-1$
							}, 0);
					prefDialog.open();
				}
			}
			return;
		}

		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.pasteAction", null); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		/*
		 * Fixed bug 231728. We decide to always enable Paste action and do check only
		 * before run it. Some eclipse editors already take that approach. It's just
		 * reproduced on Editor, but not view.
		 */
		return true;
	}

	private boolean canPaste(List info) {
		return validateCanPaste(getSelection(), getClipBoardContents(), info);
	}

	protected Object getClipBoardContents() {
		return Clipboard.getDefault().getContents();
	}

	public Object getSelection() {
		Object selection = super.getSelection();
		if (selection instanceof StructuredSelection) {
			selection = ((StructuredSelection) selection).getFirstElement();
		}
		return selection;
	}

	public static boolean validateCanPaste(Object targetObj, Object transferData, List infoList) {
		return (infoList == null ? (DNDUtil.handleValidateTargetCanContain(targetObj, transferData))
				: DNDUtil.handleValidateTargetCanContain(targetObj, transferData, infoList))
				&& DNDUtil.handleValidateTargetCanContainMore(targetObj, DNDUtil.getObjectLength(transferData));
	}

}