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

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * This class represents the edit action
 * 
 * 
 */
public class EditAction extends AbstractElementAction {

	/**
	 * the default text
	 */
	public static final String TEXT = Messages.getString("EditAction.text"); //$NON-NLS-1$

	/**
	 * Create a new edit action with given selection and default text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * 
	 */
	public EditAction(Object selectedObject) {
		this(selectedObject, TEXT);
	}

	/**
	 * Create a new edit action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public EditAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	public boolean isEnabled() {
		if (getSelectedElement() != null) {
			return getSelectedElement().canEdit();
		} else if (getSelectedElementDetail() != null) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		if (getSelectedElement() != null) {
			return ProviderFactory.createProvider(getSelectedElement()).performRequest(getSelectedElement(),
					new Request(IRequestConstants.REQUEST_TYPE_EDIT));
		} else if (getSelectedElementDetail() != null) {
			return ProviderFactory.createProvider(getSelectedElementDetail()).performRequest(getSelectedElementDetail(),
					new Request(IRequestConstants.REQUEST_TYPE_EDIT));
		}
		return false;

	}

	/**
	 * @return the model of selected GUI object.
	 */
	private DesignElementHandle getSelectedElement() {
		Object obj = super.getSelection();
		if (obj instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) obj;
			if (selection.size() != 1) {// multiple selection
				return null;
			}
			obj = selection.getFirstElement();
		}
		if (obj instanceof DesignElementHandle) {
			return (DesignElementHandle) obj;
		}
		return null;
	}

	private ElementDetailHandle getSelectedElementDetail() {
		Object obj = super.getSelection();
		if (obj instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) obj;
			if (selection.size() != 1) {// multiple selection
				return null;
			}
			obj = selection.getFirstElement();
		}
		if (obj instanceof ElementDetailHandle) {
			return (ElementDetailHandle) obj;
		}
		return null;
	}

}