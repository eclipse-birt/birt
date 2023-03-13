/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.FilterModelProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;

/**
 * The class is responsible to manage chart filter with cube set.
 *
 * @since 2.3
 */
public class ChartCubeFilterModelProvider extends FilterModelProvider {

	public ChartCubeFilterModelProvider() {
		fFilterPropertyName = ChartReportItemUtil.PROPERTY_CUBE_FILTER;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.providers.
	 * FilterModelProvider#getElements(java.util.List)
	 */
	@Override
	public Object[] getElements(List input) {
		Object obj = input.get(0);
		if (!(obj instanceof DesignElementHandle)) {
			return EMPTY;
		}
		DesignElementHandle element = (DesignElementHandle) obj;
		PropertyHandle propertyHandle = element.getPropertyHandle(fFilterPropertyName);
		Iterator iterator = propertyHandle.getListValue().iterator();
		if (iterator == null) {
			return EMPTY;
		}
		List list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list.toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.providers.
	 * FilterModelProvider#getText(java.lang.Object, java.lang.String)
	 */
	@Override
	public String getText(Object element, String key) {
		if (element instanceof FilterConditionElementHandle) {
			String value = ((FilterConditionElementHandle) element).getStringProperty(key);
			if (value == null) {
				value = "";//$NON-NLS-1$
			}

			if (key.equals(IFilterConditionElementModel.OPERATOR_PROP)) {
				IChoice choice = choiceSet.findChoice(value);
				if (choice != null) {
					return choice.getDisplayName();
				}
			} else {
				return value;
			}

			return "";//$NON-NLS-1$
		}

		return super.getText(element, key);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.providers.
	 * FilterModelProvider#deleteItem(java.lang.Object, int)
	 */
	@Override
	public boolean deleteItem(Object item, int pos) throws PropertyValueException {
		DesignElementHandle element = (DesignElementHandle) item;
		PropertyHandle propertyHandle = element.getPropertyHandle(fFilterPropertyName);
		if (propertyHandle.getListValue().get(pos) != null) {
			propertyHandle.removeItem(pos);
		}

		return true;
	}
}
