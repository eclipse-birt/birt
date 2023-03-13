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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.GridHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.gef.EditPart;

/**
 *
 */

public class TableElementBreadcrumbNodeProvider extends DefaultBreadcrumbNodeProvider {

	@Override
	public Object getParent(Object element) {
		Object model = getRealModel(element);
		if (model instanceof RowHandle || model instanceof ColumnHandle) {
			return getEditPartModel(model);
		}

		return super.getParent(element);
	}

	@Override
	public Object[] getChildren(Object element) {
		Object model = getRealModel(element);

		if (model instanceof RowHandle) {
			return getChildrenBySlotHandle(((RowHandle) model).getCells());
		}
		if (model instanceof TableHandle) {
			TableHandleAdapter adapter = HandleAdapterFactory.getInstance().getTableHandleAdapter((TableHandle) model);
			List list = new ArrayList(adapter.getRows());
			list.addAll(adapter.getColumns());
			return list.toArray();
		}
		if (model instanceof GridHandle) {
			GridHandleAdapter adapter = HandleAdapterFactory.getInstance().getGridHandleAdapter((GridHandle) model);
			List list = new ArrayList(adapter.getRows());
			list.addAll(adapter.getColumns());
			return list.toArray();
		}
		return new Object[0];
	}

	protected Object[] getChildrenBySlotHandle(SlotHandle slotHandle) {
		ArrayList list = new ArrayList();
		Iterator itor = slotHandle.iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle eleHandle = (DesignElementHandle) obj;
				list.add(eleHandle);
			}
		}

		return list.toArray();
	}

	@Override
	public Object getRealModel(Object element) {
		if (element instanceof EditPart) {
			EditPart editpart = (EditPart) element;
			return editpart.getModel();
		}
		return element;
	}
}
