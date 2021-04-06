/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
