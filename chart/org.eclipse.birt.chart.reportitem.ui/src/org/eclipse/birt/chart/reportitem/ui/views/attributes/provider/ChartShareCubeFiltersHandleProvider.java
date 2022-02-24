/*******************************************************************************
* Copyright (c) 2007 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * 
 */

public class ChartShareCubeFiltersHandleProvider extends ChartCubeFilterHandleProvider {

	public ChartShareCubeFiltersHandleProvider(AbstractFilterHandleProvider baseProvider) {
		super(baseProvider);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List<?>) {
			List<Object> elements = new ArrayList<Object>();
			for (Iterator<Object> iter = ((List<Object>) inputElement).iterator(); iter.hasNext();) {
				DesignElementHandle handle = (DesignElementHandle) iter.next();
				if (handle instanceof ReportItemHandle
						&& ((ReportItemHandle) handle).getDataBindingReference() != null) {
					elements.add(ChartReportItemUtil.getReportItemReference((ReportItemHandle) handle));
				} else {
					elements.add(handle);
				}
			}
			setContentInput(elements);
		} else {
			List<Object> contentInput = new ArrayList<Object>();
			if (inputElement instanceof ReportItemHandle
					&& ((ReportItemHandle) inputElement).getDataBindingReference() != null) {
				contentInput.add(((ReportItemHandle) inputElement).getDataBindingReference());
			} else {
				contentInput.add(inputElement);
			}
			setContentInput(contentInput);
		}

		Object[] elements = getModelAdapter().getElements(getContentInput());
		return elements;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

}
