/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
