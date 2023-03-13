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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * The class is used for chart sharing bindings/fitlers with crosstab.
 *
 * @since 2.3
 */
public class ChartShareCrosstabFiltersHandleProvider extends CrosstabFilterHandleProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * IFormHandleProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof List) {
			List elements = new ArrayList();
			for (Iterator iter = ((List) inputElement).iterator(); iter.hasNext();) {
				DesignElementHandle handle = ((DesignElementHandle) iter.next());
				if (ChartReportItemUtil.isChildOfMultiViewsHandle(handle)) {
					elements.add(handle.getContainer().getContainer());
				} else if (handle instanceof ReportItemHandle
						&& ((ReportItemHandle) handle).getDataBindingReference() != null) {
					elements.add(ChartReportItemUtil.getReportItemReference((ReportItemHandle) handle));
				} else {
					elements.add(handle);
				}
			}
			contentInput = elements;
		} else {
			contentInput = new ArrayList();
			if (inputElement instanceof DesignElementHandle
					&& ChartReportItemUtil.isChildOfMultiViewsHandle((DesignElementHandle) inputElement)) {
				contentInput.add(((DesignElementHandle) inputElement).getContainer().getContainer());
			} else if (inputElement instanceof ReportItemHandle
					&& ((ReportItemHandle) inputElement).getDataBindingReference() != null) {
				contentInput.add(((ReportItemHandle) inputElement).getDataBindingReference());
			} else {
				contentInput.add(inputElement);
			}
		}

		Object[] elements = modelAdapter.getElements(contentInput);
		return elements;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * AbstractFormHandleProvider#isEditable()
	 */
	@Override
	public boolean isEditable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.
	 * CrosstabFilterHandleProvider#getConcreteFilterProvider()
	 */
	@Override
	public IFormProvider getConcreteFilterProvider() {
		if (input == null) {
			return this;
		}

		return ChartFilterProviderDelegate.createFilterProvider(input, getInput());
	}
}
