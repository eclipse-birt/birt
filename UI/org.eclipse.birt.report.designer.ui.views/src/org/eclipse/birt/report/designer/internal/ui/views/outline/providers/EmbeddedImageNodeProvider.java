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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.EmbeddedImageNode;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertEmbeddedImageAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Node provider for embedded images
 */

public class EmbeddedImageNodeProvider extends DefaultNodeProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#
	 * getIconName(java.lang.Object)
	 */
	public String getIconName(Object model) {
		assert (model instanceof EmbeddedImageHandle);
		// EmbeddedImageHandle image = (EmbeddedImageHandle)model;

		return IReportGraphicConstants.ICON_ELEMENT_IMAGE;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName(Object model) {
		return ((EmbeddedImageHandle) model).getQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getParent(
	 * java.lang.Object)
	 */
	public Object getParent(Object model) {
		return new EmbeddedImageNode(SessionHandleAdapter.getInstance().getReportDesignHandle());
	}

	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param object the object
	 * @param menu   the menu
	 */
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		menu.add(new InsertEmbeddedImageAction(object, Messages.getString("EmbeddedImageNodeProvider.action.New"))); //$NON-NLS-1$
		super.createContextMenu(sourceViewer, object, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * performRequest(java.lang.Object, org.eclipse.gef.Request)
	 */
	public boolean performRequest(Object model, Request request) throws Exception {
		if (request.getType().equals(IRequestConstants.REQUEST_TYPE_INSERT)) {
			return ProviderFactory.createProvider(getParent(model)).performRequest(model, request);
		}
		return false;
	}
}
