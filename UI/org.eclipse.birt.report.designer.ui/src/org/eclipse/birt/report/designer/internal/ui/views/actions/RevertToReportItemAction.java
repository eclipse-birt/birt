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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * 
 */

public class RevertToReportItemAction extends AbstractElementAction {

	private static final String DEFAULT_TEXT = Messages.getString("RevertToReportItemAction.text"); //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public RevertToReportItemAction(Object selectedObject) {
		super(selectedObject, DEFAULT_TEXT);
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public RevertToReportItemAction(Object selectedObject, String text) {
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
		if (getSelectedElement().isTemplateParameterValue()) {
			DesignElementHandle handle;
			if (getSelectedElement() instanceof TemplateReportItemHandle) {
				TemplateReportItemHandle template = (TemplateReportItemHandle) getSelectedElement();
				handle = template.copyDefaultElement()
						.getHandle(SessionHandleAdapter.getInstance().getReportDesignHandle().getModule());
				try {
					template.transformToReportItem((ReportItemHandle) handle);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
					return false;
				}
			} else {
				handle = (DesignElementHandle) getSelectedElement();
			}
			return ProviderFactory.createProvider(getSelectedElement()).performRequest(handle,
					new Request(IRequestConstants.REQUST_REVERT_TO_REPORTITEM));
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
		if (obj instanceof TemplateReportItemHandle) {
			return (TemplateReportItemHandle) obj;
		} else if (obj instanceof DesignElementHandle) {
			return (DesignElementHandle) obj;
		}
		return null;
	}

	public boolean isEnabled() {
		if (getSelectedElement() == null) {
			return false;
		}
		return super.isEnabled() && (getSelectedElement().isTemplateParameterValue());
	}
}
