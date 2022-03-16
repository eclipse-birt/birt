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

package org.eclipse.birt.report.designer.internal.lib.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.lib.editparts.EmptyEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DesignerBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class LibraryBreadcrumbNodeProvider extends DesignerBreadcrumbNodeProvider {

	@Override
	public boolean validate(Object element) {
		if (getEditPart(element) == null
		// || getEditPart( element ) instanceof EmptyEditPart
		) {
			return false;
		}
		return true;
	}

	@Override
	public EditPart getEditPart(Object element) {
		// EditPart editPart = super.getEditPart( element );
		// if ( editPart == null || editPart instanceof EmptyEditPart )
		// return null;
		return super.getEditPart(element);
	}

	@Override
	public Object[] getChildren(Object element) {
		if (getRealModel(element) instanceof LibraryHandle) {
			return ((LibraryHandle) getRealModel(element)).getComponents().getContents().toArray();
		}
		List children = new ArrayList(Arrays.asList(super.getChildren(element)));
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i) instanceof EmptyEditPart) {
				children.remove(i);
				i--;
			}
		}
		return children.toArray();
	}

	@Override
	public String getText(Object element) {
		Object object = getRealModel(element);
		if (getEditPart(object) == null) {
			if (object instanceof DesignElementHandle
					&& ((DesignElementHandle) object).getContainer() instanceof LibraryHandle) {
				INodeProvider provider = ProviderFactory.createProvider(object);
				if (provider == null) {
					return object.toString();
				}
				return provider.getNodeDisplayName(object);
			}
		}
		return super.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		Object object = getRealModel(element);
		if (getEditPart(object) == null) {
			if (object instanceof DesignElementHandle
					&& ((DesignElementHandle) object).getContainer() instanceof LibraryHandle) {
				INodeProvider provider = ProviderFactory.createProvider(object);
				if (provider == null) {
					return null;
				}
				return provider.getNodeIcon(object);
			}
		}
		return super.getImage(element);
	}

	@Override
	public String getTooltipText(Object element) {
		Object object = getRealModel(element);
		if (getEditPart(object) == null) {
			if (object instanceof DesignElementHandle
					&& ((DesignElementHandle) object).getContainer() instanceof LibraryHandle) {
				INodeProvider provider = ProviderFactory.createProvider(object);
				if (provider == null) {
					return object.toString();
				}
				return provider.getNodeTooltip(object);
			}
		}
		return super.getTooltipText(element);
	}
}
