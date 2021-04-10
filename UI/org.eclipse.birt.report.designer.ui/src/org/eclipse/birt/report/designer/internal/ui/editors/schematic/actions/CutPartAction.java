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

import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Cut action
 */
public class CutPartAction extends WrapperSelectionAction {

	/**
	 * Create a new cut action with given selection and text
	 * 
	 * @param part the selected object,which cannot be null
	 */
	public CutPartAction(IWorkbenchPart part) {
		super(part);
		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		setDisabledImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		setAccelerator(SWT.CTRL | 'X');// $NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#getId()
	 */
	public String getId() {
		return ActionFactory.CUT.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
	 * WrapperSelectionAction#createActionHandler(org.eclipse.jface.viewers.
	 * ISelection)
	 */
	protected IAction createActionHandler(ISelection model) {
		return new CutAction(model);
	}
}