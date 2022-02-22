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

package org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class DesignerBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	@Override
	public Object getParent(Object element) {
		if (getEditPart(element) == null) {
			return null;
		}

		Object adapter = getEditPart(element).getAdapter(IBreadcrumbNodeProvider.class);
		if (adapter instanceof DefaultBreadcrumbNodeProvider) {
			((DefaultBreadcrumbNodeProvider) adapter).setContext(viewer);
			return ((IBreadcrumbNodeProvider) adapter).getParent(element);
		}

		return super.getParent(element);
	}

	@Override
	public Object[] getChildren(Object element) {
		if (getEditPart(element) == null) {
			return new Object[0];
		}

		Object adapter = getEditPart(element).getAdapter(IBreadcrumbNodeProvider.class);
		if (adapter instanceof DefaultBreadcrumbNodeProvider) {
			((DefaultBreadcrumbNodeProvider) adapter).setContext(viewer);
			return ((IBreadcrumbNodeProvider) adapter).getChildren(element);
		}

		return super.getChildren(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		return super.hasChildren(element);
	}

	@Override
	public Image getImage(Object element) {
		if (getEditPart(element) == null) {
			return null;
		}

		Object adapter = getEditPart(element).getAdapter(IBreadcrumbNodeProvider.class);
		if (adapter instanceof IBreadcrumbNodeProvider) {
			((DefaultBreadcrumbNodeProvider) adapter).setContext(viewer);
			return ((DefaultBreadcrumbNodeProvider) adapter).getImage(element);
		}

		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (getEditPart(element) == null) {
			return null;
		}

		Object adapter = getEditPart(element).getAdapter(IBreadcrumbNodeProvider.class);
		if (adapter instanceof DefaultBreadcrumbNodeProvider) {
			((DefaultBreadcrumbNodeProvider) adapter).setContext(viewer);
			return ((IBreadcrumbNodeProvider) adapter).getText(element);
		}

		return super.getText(element);
	}

	@Override
	public String getTooltipText(Object element) {
		if (getEditPart(element) == null) {
			return null;
		}

		Object adapter = getEditPart(element).getAdapter(IBreadcrumbNodeProvider.class);
		if (adapter instanceof DefaultBreadcrumbNodeProvider) {
			((DefaultBreadcrumbNodeProvider) adapter).setContext(viewer);
			return ((IBreadcrumbNodeProvider) adapter).getTooltipText(element);
		}

		return super.getTooltipText(element);
	}

	@Override
	public void createContextMenu(Object element, IMenuManager menu) {
		if (getEditPart(element) == null) {
			return;
		}

		Object adapter = getEditPart(element).getAdapter(IBreadcrumbNodeProvider.class);
		if (adapter instanceof DefaultBreadcrumbNodeProvider) {
			((DefaultBreadcrumbNodeProvider) adapter).setContext(viewer);
			((IBreadcrumbNodeProvider) adapter).createContextMenu(element, menu);
			return;
		}

		super.createContextMenu(element, menu);
	}

}
