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
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 *
 */

public class RevertToTemplateAction extends AbstractElementAction {

	private static final String DEFAULT_TEXT = Messages.getString("RevertToTemplateAction.text"); //$NON-NLS-1$

	public RevertToTemplateAction(Object selectedObject) {
		super(selectedObject, DEFAULT_TEXT);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public RevertToTemplateAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.
	 * AbstractElementAction#doAction()
	 */
	@Override
	protected boolean doAction() throws Exception {
		if (getSelectedElement() == null) {
			return false;
		}

		else if (getSelectedElement().isTemplateParameterValue()) {
			return ProviderFactory.createProvider(getSelectedElement()).performRequest(getSelectedElement(),
					new Request(IRequestConstants.REQUST_REVERT_TO_TEMPLATEITEM));
		}
		return false;
	}

	/**
	 * @return the model of selected GUI object.
	 */
	DesignElementHandle getSelectedElement() {
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

	@Override
	public boolean isEnabled() {
		if (getSelectedElement() == null) {
			return false;
		}

		return super.isEnabled() && getSelectedElement().canTransformToTemplate()
				&& getSelectedElement().isTemplateParameterValue()
				// Remove this type check to fix bug 148330
				// && !( getSelectedElement( ).getContainer( ) instanceof SimpleMasterPageHandle
				// )
				&& !(getSelectedElement().getRoot() instanceof LibraryHandle);
	}

}
