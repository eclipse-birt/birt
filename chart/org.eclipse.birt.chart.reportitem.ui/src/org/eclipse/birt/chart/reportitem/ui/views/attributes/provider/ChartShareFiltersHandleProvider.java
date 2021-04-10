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

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * The class is used for chart sharing binding/filters case.
 * 
 * @since 2.3
 */
public class ChartShareFiltersHandleProvider extends ChartFilterProviderDelegate {

	public ChartShareFiltersHandleProvider(AbstractFilterHandleProvider baseProvider) {
		super(baseProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * FilterHandleProvider#isEditable()
	 */
	public boolean isEditable() {
		if (getInput() == null) {
			return false;
		}

		boolean editable = super.isEditable();

		// Don't allow to edit filter in chart property page when chart is in
		// multi-views, so return false.
		if (ChartReportItemUtil.isChildOfMultiViewsHandle(((ReportItemHandle) DEUtil.getInputFirstElement(input)))) {
			return false;
		}

		return editable;
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
}
