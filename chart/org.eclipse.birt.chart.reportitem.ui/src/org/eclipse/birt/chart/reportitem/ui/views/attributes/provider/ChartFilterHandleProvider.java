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

import java.util.List;

import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * Chart filter handle provider.
 * 
 * @since 2.3
 */
public class ChartFilterHandleProvider extends ChartFilterProviderDelegate {

	public ChartFilterHandleProvider(AbstractFilterHandleProvider baseProvider) {
		super(baseProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.
	 * CrosstabFilterHandleProvider#getConcreteFilterProvider()
	 */
	public IFormProvider getConcreteFilterProvider() {
		if (input == null) {
			return this;
		}

		return ChartFilterProviderDelegate.createFilterProvider(input, getInput());
	}

	@Override
	public boolean isEditable() {
		if (((ReportItemHandle) DEUtil.getInputFirstElement(super.input))
				.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
			return false;
		Object handle = null;
		if (input instanceof List<?>) {
			handle = ((List<?>) input).get(0);
		} else {
			handle = input;
		}
		ReportItemHandle rih = (ReportItemHandle) handle;
		return !ChartItemUtil.isChartInheritGroups(rih);
	}
}
