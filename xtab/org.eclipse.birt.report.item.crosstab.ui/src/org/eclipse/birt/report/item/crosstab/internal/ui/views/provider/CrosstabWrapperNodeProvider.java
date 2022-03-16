/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.gef.Request;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class CrosstabWrapperNodeProvider extends DefaultNodeProvider {

	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		if (object instanceof CrosstabPropertyHandleWrapper) {
			super.createContextMenu(sourceViewer, ((CrosstabPropertyHandleWrapper) object).getModel(), menu);
		} else {
			super.createContextMenu(sourceViewer, object, menu);
		}
	}

	@Override
	public String getNodeDisplayName(Object model) {
		if (model instanceof CrosstabPropertyHandleWrapper) {
			return super.getNodeDisplayName(((CrosstabPropertyHandleWrapper) model).getModel());
		} else {
			return super.getNodeDisplayName(model);
		}
	}

	@Override
	public Image getNodeIcon(Object model) {
		if (model instanceof CrosstabPropertyHandleWrapper) {
			return super.getNodeIcon(((CrosstabPropertyHandleWrapper) model).getModel());
		} else {
			return super.getNodeIcon(model);
		}
	}

	@Override
	public String getNodeTooltip(Object model) {
		if (model instanceof CrosstabPropertyHandleWrapper) {
			return super.getNodeTooltip(((CrosstabPropertyHandleWrapper) model).getModel());
		} else {
			return super.getNodeTooltip(model);
		}
	}

	@Override
	public Object[] getChildren(Object object) {
		if (object instanceof CrosstabPropertyHandleWrapper) {
			return super.getChildren(((CrosstabPropertyHandleWrapper) object).getModel());
		} else {
			return super.getChildren(object);
		}
	}

	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof CrosstabPropertyHandleWrapper) {
			return super.hasChildren(((CrosstabPropertyHandleWrapper) object).getModel());
		} else {
			return super.hasChildren(object);
		}
	}

	@Override
	public Object getParent(Object model) {
		if (model instanceof CrosstabPropertyHandleWrapper) {
			return super.getParent(((CrosstabPropertyHandleWrapper) model).getModel());
		} else {
			return super.getParent(model);
		}
	}

	@Override
	public boolean performRequest(Object model, Request request) throws Exception {
		if (model instanceof CrosstabPropertyHandleWrapper) {
			return super.performRequest(((CrosstabPropertyHandleWrapper) model).getModel(), request);
		} else {
			return super.performRequest(model, request);
		}
	}

	@Override
	public boolean isReadOnly(Object model) {
		if (model instanceof CrosstabPropertyHandleWrapper) {
			return super.isReadOnly(((CrosstabPropertyHandleWrapper) model).getModel());
		} else {
			return super.isReadOnly(model);
		}
	}

}
