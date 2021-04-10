/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DefaultBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.IBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.gef.EditPart;

/**
 * 
 */

public class CrosstabBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	public Object getRealModel(Object element) {
		if (element instanceof EditPart) {
			EditPart editpart = (EditPart) element;
			return editpart.getModel();
		}
		return element;
	}

	public Object getParent(Object element) {
		Object parent = ProviderFactory.createProvider(element).getParent(element);
		if (parent instanceof ExtendedItemHandle && ICrosstabConstants.CROSSTAB_EXTENSION_NAME
				.equals(((ExtendedItemHandle) parent).getExtensionName())) {
			return parent;
		}
		return super.getParent(element);
	}

	public Object[] getChildren(Object object) {
		Object element = getRealModel(object);
		Object parent = ProviderFactory.createProvider(element).getParent(element);
		if (parent instanceof ExtendedItemHandle && ICrosstabConstants.CROSSTAB_EXTENSION_NAME
				.equals(((ExtendedItemHandle) parent).getExtensionName())) {
			List children = getEditPart(parent).getChildren();
			List elements = new ArrayList();
			for (int i = 0; i < children.size(); i++) {
				EditPart child = ((EditPart) children.get(i));
				Object adapter = child.getAdapter(IBreadcrumbNodeProvider.class);
				if (adapter instanceof CrosstabCellBreadcrumbNodeProvider) {
					((CrosstabCellBreadcrumbNodeProvider) adapter).setContext(viewer);
					if (element.equals(((CrosstabCellBreadcrumbNodeProvider) adapter).getParent(child))) {
						elements.add(child);
					}
				}
			}
			return elements.toArray();
		} else {
			return ProviderFactory.createProvider(element).getChildren(element);
		}
	}
}
