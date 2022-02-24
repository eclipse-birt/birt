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
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.gef.Request;

public class TransferPlaceHolderAction extends CreatePlaceHolderAction {

	public TransferPlaceHolderAction(Object selectedObject) {
		super(selectedObject, "transfer place Holder"); //$NON-NLS-1$
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
				new Request(IRequestConstants.REQUEST_TRANSFER_PLACEHOLDER));
	}

	public boolean isEnabled() {
		return getSelectedElement() instanceof TemplateElementHandle
				&& (((TemplateElementHandle) getSelectedElement()).getDefaultElement() != null);
	}

}
