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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportRootEditPart;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class DefaultBreadcrumbNodeProvider implements IBreadcrumbNodeProvider, IBreadcrumbValidator {

	protected GraphicalViewer viewer;

	@Override
	public Object getParent(Object element) {
		Object model = getRealModel(element);
		if (model == null) {
			return null;
		}
		EditPart parent = getEditPart(model).getParent();
		if (parent instanceof MultipleEditPart) {
			parent = parent.getParent();
		}
		if (parent != null && !(parent instanceof ReportRootEditPart)) {
			return parent.getModel();
		} else {
			return null;
		}
	}

	@Override
	public Object[] getChildren(Object element) {
		Object model = getRealModel(element);
		if ((model == null) || (getEditPart(element) == null)) {
			return new Object[0];
		}
		List children = getEditPart(model).getChildren();
		if (children == null) {
			return new Object[0];
		} else {
			List childrenCopy = new ArrayList(children);
			List<MultipleEditPart> multipleEditParts = new ArrayList<>();
			for (int i = 0; i < childrenCopy.size(); i++) {
				if (childrenCopy.get(i) instanceof MultipleEditPart) {
					multipleEditParts.add((MultipleEditPart) childrenCopy.get(i));
				}
			}

			for (int i = 0; i < multipleEditParts.size(); i++) {
				MultipleEditPart editPart = multipleEditParts.get(i);
				int index = childrenCopy.indexOf(editPart);
				childrenCopy.remove(index);
				childrenCopy.add(index, editPart.getChildren().get(0));
			}
			return childrenCopy.toArray();
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public Image getImage(Object element) {
		Object model = getRealModel(element);
		if (model == null) {
			return null;
		}
		INodeProvider provider = ProviderFactory.createProvider(model);
		if (provider == null) {
			return null;
		}
		return provider.getNodeIcon(model);
	}

	@Override
	public String getText(Object element) {
		Object model = getRealModel(element);
		if (model == null) {
			return ""; //$NON-NLS-1$
		}
		INodeProvider provider = ProviderFactory.createProvider(model);
		if (provider == null) {
			return model.toString();
		}
		return provider.getNodeDisplayName(model);
	}

	@Override
	public String getTooltipText(Object element) {
		Object model = getRealModel(element);
		if (model == null) {
			return ""; //$NON-NLS-1$
		}
		INodeProvider provider = ProviderFactory.createProvider(model);
		if (provider == null) {
			return model.toString();
		}
		String tooltip = provider.getNodeTooltip(model);
		if (tooltip == null) {
			return getText(element);
		} else {
			return tooltip;
		}
	}

	@Override
	public void createContextMenu(Object element, IMenuManager menu) {
		Object model = getRealModel(element);
		if (model == null) {
			return;
		}
		ProviderFactory.createProvider(model).createContextMenu(null, model, menu);
	}

	public void setContext(Object context) {
		if (context instanceof GraphicalViewer) {
			viewer = (GraphicalViewer) context;
		}
	}

	@Override
	public boolean validate(Object element) {
		if (getEditPart(element) == null) {
			return false;
		}
		return true;
	}

	public EditPart getEditPart(Object element) {
		EditPart part = null;
		if (element instanceof EditPart) {
			return (EditPart) element;
		}
		if (element instanceof SlotHandle) {
			part = (EditPart) viewer.getEditPartRegistry().get(new ListBandProxy((SlotHandle) element));
		} else {
			part = (EditPart) viewer.getEditPartRegistry().get(element);
		}

		if (part == null) {
			part = getInterestEditPart(viewer.getRootEditPart(), element);
		}
		if (part == null) {
			if (getEditPartModel(element) != null) {
				part = getEditPart(getEditPartModel(element));
			}
		}

		return part;
	}

	protected Object getEditPartModel(Object element) {
		if (element instanceof RowHandle || element instanceof ColumnHandle) {
			while (element instanceof ReportElementHandle) {
				if (((ReportElementHandle) element).getContainer() instanceof ListingHandle
						|| ((ReportElementHandle) element).getContainer() instanceof GridHandle) {
					return ((ReportElementHandle) element).getContainer();
				} else {
					element = ((ReportElementHandle) element).getContainer();
				}
			}
		}
		return null;
	}

	private EditPart getInterestEditPart(EditPart part, Object obj) {
		List chList = part.getChildren();
		for (int i = 0; i < chList.size(); i++) {
			ReportElementEditPart reportEditPart = (ReportElementEditPart) chList.get(i);
			if (reportEditPart.isinterestSelection(obj)) {
				return reportEditPart;
			} else {
				EditPart retValue = getInterestEditPart(reportEditPart, obj);
				if (retValue != null) {
					return retValue;
				}
			}
		}
		return null;
	}

	public Object getRealModel(Object element) {
		EditPart editpart = null;
		if (!(element instanceof EditPart)) {
			editpart = getEditPart(element);
		} else {
			editpart = (EditPart) element;
		}

		if (editpart != null) {
			Object model = editpart.getAdapter(IBreadcrumbNodeProvider.class);
			if (model == null) {
				return editpart.getModel();
			} else if (model instanceof DefaultBreadcrumbNodeProvider) {
				((DefaultBreadcrumbNodeProvider) model).setContext(viewer);
				return ((DefaultBreadcrumbNodeProvider) model).getRealModel(element);
			} else {
				return model;
			}
		}
		return element;
	}
}
