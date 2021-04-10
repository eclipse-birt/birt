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
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ChangeDataColumnAction extends AbstractElementAction {

	private static final String DEFAULT_TEXT = Messages.getString("ChangeDataColumnAction.text"); //$NON-NLS-1$

	public ChangeDataColumnAction(Object selectedObject) {
		super(selectedObject, DEFAULT_TEXT);
	}

	public ChangeDataColumnAction(Object selectedObject, String text) {
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
				new Request(IRequestConstants.REQUEST_CHANGE_DATA_COLUMN));
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

}
