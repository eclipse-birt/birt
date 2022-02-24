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

import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DefaultBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.BaseCrosstabAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.gef.EditPart;

/**
 * 
 */

public class CrosstabCellBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	public Object getRealModel(Object element) {
		EditPart editpart = null;
		if (!(element instanceof EditPart)) {
			editpart = getEditPart(element);
		} else
			editpart = (EditPart) element;

		if (editpart != null && editpart.getModel() instanceof BaseCrosstabAdapter) {
			if (editpart.getModel() instanceof VirtualCrosstabCellAdapter) {
				return element;
			}
			return ((BaseCrosstabAdapter) editpart.getModel()).getDesignElementHandle();
		}
		return element;
	}

	public Object getParent(Object element) {
		Object model = getRealModel(element);
		if (model instanceof ExtendedItemHandle)
			return getAvailableParent(model);
		return super.getParent(element);
	}

	private Object getAvailableParent(Object element) {
		Object model = element;
		while (true) {
			INodeProvider provider = ProviderFactory.createProvider(model);
			model = provider.getParent(model);
			if (model == null)
				return null;
			else if (model instanceof ExtendedItemHandle && ICrosstabConstants.CROSSTAB_EXTENSION_NAME
					.equals(((ExtendedItemHandle) model).getExtensionName())) {
				return model;
			}
			Object parent = ProviderFactory.createProvider(model).getParent(model);
			if (parent instanceof ExtendedItemHandle && ICrosstabConstants.CROSSTAB_EXTENSION_NAME
					.equals(((ExtendedItemHandle) parent).getExtensionName())) {
				return model;
			}
		}
	}
}
