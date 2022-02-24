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

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

public class CreatePlaceHolderAction extends AbstractElementAction {

	private static final String DEFAULT_TEXT = Messages.getString("CreatePlaceHolderAction.text"); //$NON-NLS-1$

	public CreatePlaceHolderAction(Object selectedObject) {
		super(selectedObject, DEFAULT_TEXT);
	}

	public CreatePlaceHolderAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	protected boolean doAction() throws Exception {
		if (getSelectedElement() == null) {
			return false;
		}

		return ProviderFactory.createProvider(getSelectedElement()).performRequest(getSelectedElement(),
				new Request(IRequestConstants.REQUEST_CREATE_PLACEHOLDER));
	}

	/**
	 * @return the model of selected GUI object.
	 */
	ReportElementHandle getSelectedElement() {
		Object obj = super.getSelection();
		if (obj instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) obj;
			if (selection.size() != 1) {// multiple selection
				return null;
			}
			obj = selection.getFirstElement();
		}
		if (obj instanceof ReportElementHandle) {
			return (ReportElementHandle) obj;
		}
		return null;
	}

	public boolean isEnabled() {
		return super.isEnabled() && getSelectedElement() instanceof ReportItemHandle
				&& getSelectedElement().canTransformToTemplate()
				// Can't create place holder in Simple Master Page
				&& !(getSelectedElement().getRoot() instanceof LibraryHandle);
	}
}
