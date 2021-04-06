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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Copy action
 */
public class CopyPartAction extends WrapperSelectionAction {

	/**
	 * Create a new copy action with given selection and text
	 * 
	 * @param part the selected object,which cannot be null
	 */
	public CopyPartAction(IWorkbenchPart part) {
		super(part);
		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setAccelerator(SWT.CTRL | 'C');// $NON-NLS-1$
	}

	public String getId() {
		return ActionFactory.COPY.getId();
	}

	/**
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.WrapperSelectionAction#createActionHandler(org.eclipse.jface.viewers.ISelection)
	 */
	protected IAction createActionHandler(ISelection model) {
		return new CopyAction(model);
	}
}